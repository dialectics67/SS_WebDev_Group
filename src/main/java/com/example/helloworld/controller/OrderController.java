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
    public String create(@RequestParam("group_id") Integer group_id, @RequestParam("building_id") Integer building_id, HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        // 获取用户id
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 检查group_id 是否一致
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUid(uid, Consts.IS_NOT_DEL);
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

    public String process() throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");

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
        List<OrdersEntity> ordersEntityList = ordersService.findAllByUid(uid);
        for (OrdersEntity ordersEntity : ordersEntityList) {
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
        // 判断订单是否由用户发起
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        if (!Objects.equals(ordersEntity.getUid(), uid)) {
            res.put("code", 515202);
            res.put("message", "无权获取该订单信息");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 成功，返回订单信息
        resData.put("status", ordersEntity.getStatus());
        resData.put("room_id", ordersEntity.getRoomId());
        return new ObjectMapper().writeValueAsString(res);
    }
}
