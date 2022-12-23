package com.example.helloworld.controller;

import com.example.helloworld.constants.Consts;
import com.example.helloworld.constants.RabbitConsts;
import com.example.helloworld.entity.GroupsUserEntity;
import com.example.helloworld.entity.OrdersEntity;
import com.example.helloworld.service.BuildingsService;
import com.example.helloworld.service.GroupsService;
import com.example.helloworld.service.GroupsUserService;
import com.example.helloworld.service.OrdersService;
import com.example.helloworld.utils.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private GroupsUserService groupsUserService;
    @Autowired
    private BuildingsService buildingsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private GroupsService groupsService;

    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody Map<String, String> requestBody, HttpSession session) throws JsonProcessingException {
        // 获取参数
        Integer group_id = Integer.valueOf(requestBody.get("group_id"));
        Integer building_id = Integer.valueOf(requestBody.get("building_id"));
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        // 获取用户id
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 检查group_id 是否一致
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUidOnlyNotDel(uid);
        if ((!groupsUserEntityOptional.isPresent() && group_id != 0) ||
                (groupsUserEntityOptional.isPresent() && !Objects.equals(groupsUserEntityOptional.get().getGroupId(), group_id))) {
            res.put("code", 515101);
            res.put("message", "group_id与数据库中的不一致");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断是否存在相应的building
        if (!buildingsService.findById(building_id).isPresent()) {
            res.put("code", 515102);
            res.put("message", "building_id不存在");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断自己是否为队长
//        if (groupsUserEntityOptional.isPresent() && groupsUserEntityOptional.get().getIsCreator() == Consts.GROUP_USER_IS_NOT_CREATOR) {
//            res.put("code", 515103);
//            res.put("message", "只有队长有权提交订单");
//            return new ObjectMapper().writeValueAsString(res);
//        }
        // 生成订单
        OrdersEntity ordersEntity = new OrdersEntity();
        // 填写基本数据
        ordersEntity.setUid(uid);
        ordersEntity.setGroupId(group_id);
        ordersEntity.setBuildingId(building_id);
        ordersEntity.setSubmitTime(commonUtil.getUnixTimestamp());
        ordersEntity.setCreateTime(commonUtil.getUnixTimestamp());
        ordersEntity.setStatus(Consts.ORDER_STATUS_WAIT);
        ordersEntity.setIsDel(Consts.IS_NOT_DEL);
        // 保存到数据库, 获得订单id
        ordersService.save(ordersEntity);
        // 传入消息队列
        rabbitTemplate.convertAndSend(RabbitConsts.SIMPLE_MODE_QUEUE_SUBMISSION, ordersEntity);
        // 返回order_id
        resData.put("order_id", ordersEntity.getId());
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/list")
    @ResponseBody
    public String list(HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        res.put("code", 200);
        res.put("message", "操作成功");
        res.put("data", resData);
        resData.put("rows", rows);
        // 获取用户id
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 获取以单人形式提交的订单
        List<OrdersEntity> ordersEntityList = ordersService.findAllByUid(uid);
        for (OrdersEntity ordersEntity : ordersEntityList) {
            // 暂不处理以组队形式提交的订单
            if (ordersEntity.getGroupId() != 0) {
                continue;
            }
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("order_id", ordersEntity.getId());
            Integer group_id = ordersEntity.getGroupId();
            if (group_id == 0) {
                tmp.put("group_name", "0");
            } else {
                tmp.put("group_name", groupsService.findById(group_id).get().getName());
            }
            tmp.put("building_name", buildingsService.findById(ordersEntity.getBuildingId()).get().getName());
            tmp.put("submit_time", ordersEntity.getSubmitTime());
            tmp.put("result_content", ordersEntity.getResultContent());
            tmp.put("status", ordersEntity.getStatus());
            rows.add(tmp);
        }
        // 获取以组队形式提交的且涉及到当前成员的订单
        // 首先在group members通过uid查找到(uid, group_id, join_time, leave_time)
        List<GroupsUserEntity> groupsUserEntityList = groupsUserService.findByUid(uid);
        // 依此判断与每一个有关的订单
        for (GroupsUserEntity groupsUserEntity : groupsUserEntityList) {
            Integer groupId = groupsUserEntity.getGroupId();
            Integer joinTime = groupsUserEntity.getJoinTime();
            Integer leaveTime = groupsUserEntity.getLeaveTime();
            if (leaveTime == 0) {
                leaveTime = Integer.MAX_VALUE;
            }
            // 根据groupId 查询有关的所有订单
            List<OrdersEntity> ordersEntityList1 = ordersService.findAllByGroupId(groupId);
            for (OrdersEntity ordersEntity : ordersEntityList1) {
                // 判断该订单提交时，用户是否在队伍中
                Integer submitTime = ordersEntity.getSubmitTime();
                if (submitTime > joinTime && submitTime < leaveTime) {
                    Map<String, Object> tmp = new HashMap<>();
                    tmp.put("order_id", ordersEntity.getId());
                    Integer group_id = ordersEntity.getGroupId();
                    if (group_id == 0) {
                        tmp.put("group_name", "0");
                    } else {
                        tmp.put("group_name", groupsService.findById(group_id).get().getName());
                    }
                    tmp.put("building_name", buildingsService.findById(ordersEntity.getBuildingId()).get().getName());
                    tmp.put("submit_time", ordersEntity.getSubmitTime());
                    tmp.put("result_content", ordersEntity.getResultContent());
                    tmp.put("status", ordersEntity.getStatus());
                    rows.add(tmp);
                }
            }
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/info")
    @ResponseBody
    public String info(@RequestParam("order_id") Integer order_id, HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        // 获取订单
        Optional<OrdersEntity> optionalOrdersEntity = ordersService.findById(order_id);
        // 判断订单是否存在
        if (!optionalOrdersEntity.isPresent()) {
            res.put("code", 515201);
            res.put("message", "订单不存在");
            return new ObjectMapper().writeValueAsString(res);
        }
        OrdersEntity ordersEntity = optionalOrdersEntity.get();
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 判断订单是否是单人订单
        if (ordersEntity.getGroupId() == 0) {
            // 单人订单判断是否由本人提交
            if (!Objects.equals(ordersEntity.getUid(), uid)) {
                res.put("code", 515202);
                res.put("message", "无权获取该订单信息-订单是单人订单，但您不是提交人");
                return new ObjectMapper().writeValueAsString(res);
            }
        } else {
            // 组队订单，判断时间区间是否吻合
            List<GroupsUserEntity> groupsUserEntityList = groupsUserService.findAllByUidAndGroupId(uid, ordersEntity.getGroupId());
            if (groupsUserEntityList.isEmpty()) {
                // 该队伍所有历史成员中没有该人
                res.put("code", 515202);
                res.put("message", "无权获取该订单信息-订单是组队订单，但历史成员没有该人");
                return new ObjectMapper().writeValueAsString(res);
            } else {
                Boolean flag = Boolean.FALSE;
                for (GroupsUserEntity groupsUserEntity : groupsUserEntityList) {
                    // 判断时间区间是否覆盖
                    Integer submitTime = ordersEntity.getSubmitTime();
                    Integer joinTime = groupsUserEntity.getJoinTime();
                    Integer leaveTime = groupsUserEntity.getLeaveTime();
                    if (submitTime <= joinTime || (leaveTime != 0 && submitTime >= leaveTime)) {
                        flag = Boolean.FALSE;
                    } else {
                        flag = Boolean.TRUE;
                    }

                }
                if (flag == Boolean.FALSE) {
                    res.put("code", 515202);
                    res.put("message", "无权获取该订单信息-订单提交时，成员不在队伍中");
                    return new ObjectMapper().writeValueAsString(res);
                }

            }
        }
        // 成功，返回订单信息
        resData.put("status", ordersEntity.getStatus());
        resData.put("room_id", ordersEntity.getRoomId());
        return new ObjectMapper().writeValueAsString(res);
    }
}
