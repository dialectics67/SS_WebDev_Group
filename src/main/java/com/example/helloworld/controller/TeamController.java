package com.example.helloworld.controller;


import com.example.helloworld.constants.Consts;
import com.example.helloworld.entity.GroupsEntity;
import com.example.helloworld.entity.GroupsUserEntity;
import com.example.helloworld.entity.StudentInfoEntity;
import com.example.helloworld.entity.UsersEntity;
import com.example.helloworld.service.*;
import com.example.helloworld.utils.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 组队管理器
 */
@Controller
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private SysService sysService;
    @Autowired
    private GroupsUserService groupsUserService;
    @Autowired
    private StudentInfoService studentInfoService;

    @PostMapping("/create")
    @ResponseBody
    public String create(@RequestBody Map<String, String> requestBody, HttpSession session) throws JsonProcessingException {
        // 获取参数
        String name = requestBody.get("name");
        String describe = requestBody.get("describe");
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");

        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 判断是否存在仍然有效的用户
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUidOnlyNotDel(uid);
        if (groupsUserEntityOptional.isPresent()) {
            res.put("code", 514101);
            res.put("message", "用户已经在其他队伍中");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 创建一个Group
        GroupsEntity groupsEntity = new GroupsEntity();
        groupsEntity.setName(name);
        groupsEntity.setInviteCode(UUID.randomUUID().toString());
        groupsEntity.setDescribes(describe);
        groupsEntity.setIsDel(Consts.IS_NOT_DEL);
        groupsEntity.setStatus(Consts.GROUP_STATUS_FAILED);
        groupsService.save(groupsEntity);
        // 创建一个初始队员，队长
        GroupsUserEntity groupsUserEntity = new GroupsUserEntity();
        groupsUserEntity.setUid(uid);
        groupsUserEntity.setIsCreator(Consts.GROUP_USER_IS_CREATOR);
        groupsUserEntity.setGroupId(groupsEntity.getId());
        groupsUserEntity.setIsDel(Consts.IS_NOT_DEL);
        groupsUserEntity.setJoinTime(commonUtil.getUnixTimestamp());
        groupsUserEntity.setLeaveTime(0);
        groupsUserEntity.setStatus(0);
        groupsUserService.save(groupsUserEntity);
        // 构建返回数据
        resData.put("team_id", groupsEntity.getId());
        resData.put("invite_code", groupsEntity.getInviteCode());
        return new ObjectMapper().writeValueAsString(res);
    }

    @PostMapping("/del")
    @ResponseBody
    public String del(HttpSession session) throws JsonProcessingException {
        // 获取参数
//        Integer team_id = Integer.valueOf(requestBody.get("team_id"));
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        // 获取Uid
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 获取自己当前所处的队伍
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUidOnlyNotDel(uid);
        if (!groupsUserEntityOptional.isPresent()) {
            res.put("code", 514201);
            res.put("message", "自己没有组队");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断自己是否是队长
        GroupsUserEntity groupsUserEntity = groupsUserEntityOptional.get();
        if (groupsUserEntity.getIsCreator() == Consts.GROUP_USER_IS_NOT_CREATOR) {
            res.put("code", 514202);
            res.put("message", "自己并非队长");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断队伍中是否还有其他成员
        List<GroupsUserEntity> groupsUserEntityList = groupsUserService.findAllByGroupIdAndIsDel(groupsUserEntity.getGroupId(), Consts.IS_NOT_DEL);
        if (groupsUserEntityList.size() > 1) {
            res.put("code", 514203);
            res.put("message", "队伍中仍然存在其他成员");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 执行删除
        // 标记被删除的用户
        groupsUserEntity.setIsDel(Consts.IS_DEL);
        groupsUserEntity.setLeaveTime(commonUtil.getUnixTimestamp());
        groupsUserService.save(groupsUserEntity);
        // 标记被删除的组
        GroupsEntity groupsEntity = groupsService.findById(groupsUserEntity.getGroupId()).get();
        groupsEntity.setIsDel(Consts.IS_DEL);
        groupsService.save(groupsEntity);
        return new ObjectMapper().writeValueAsString(res);
    }

    @PostMapping("/join")
    @ResponseBody
    public String join(@RequestBody Map<String, String> requestBody, HttpSession session) throws JsonProcessingException {
        // 获取参数
        String invite_code = requestBody.get("invite_code");

        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");

        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        UsersEntity usersEntity = usersService.findById(uid).get();
        // 判断用户是否在其他队伍当中
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUidOnlyNotDel(uid);
        if (groupsUserEntityOptional.isPresent()) {
            res.put("code", 514301);
            res.put("message", "用户当前正处于其他队伍当中");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 获取用户要加入的队伍
        Optional<GroupsEntity> groupsEntityOptional = groupsService.findByInviteCode(invite_code);
        if (!groupsEntityOptional.isPresent()) {
            res.put("code", 514302);
            res.put("message", "不存在对应的队伍");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断队伍是否被删除
        GroupsEntity groupsEntity = groupsEntityOptional.get();
        if (groupsEntity.getIsDel() == Consts.IS_DEL) {
            res.put("code", 514303);
            res.put("message", "该队伍已被删除");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断队伍是否已经到达人数上限
        List<GroupsUserEntity> groupsUserEntityList = groupsUserService.findAllByGroupIdAndIsDel(groupsEntity.getId(), Consts.IS_NOT_DEL);
        if (groupsUserEntityList.size() >= Integer.parseInt(sysService.findByKeyName("group_num").get().getKeyValue())) {
            res.put("code", 514304);
            res.put("message", "该队伍已达人数上限");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 获取其中一位队员
        UsersEntity mainUserEntity = usersService.findById(groupsUserEntityList.get(0).getUid()).get();
        // 判断性别是否一致
        if (!Objects.equals(usersEntity.getGender(), mainUserEntity.getGender())) {
            res.put("code", 514305);
            res.put("message", "性别不一致");
            return new ObjectMapper().writeValueAsString(res);
        }
        //创建新的 groupsUserService
        GroupsUserEntity groupsUserEntity = new GroupsUserEntity();
        groupsUserEntity.setUid(uid);
        groupsUserEntity.setIsCreator(Consts.GROUP_USER_IS_NOT_CREATOR);
        groupsUserEntity.setGroupId(groupsEntity.getId());
        groupsUserEntity.setIsDel(Consts.IS_NOT_DEL);
        groupsUserEntity.setJoinTime(commonUtil.getUnixTimestamp());
        groupsUserEntity.setLeaveTime(0);
        groupsUserEntity.setStatus(0);
        groupsUserService.save(groupsUserEntity);
        return new ObjectMapper().writeValueAsString(res);
    }

    @PostMapping("/quit")
    @ResponseBody
    public String quit(HttpSession session) throws JsonProcessingException {
        // 获取参数
//        Integer team_id = Integer.valueOf(requestBody.get("team_id"));
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");

        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 判断自己是否在队伍中
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUidOnlyNotDel(uid);
        if (!groupsUserEntityOptional.isPresent()) {
            res.put("code", 514301);
            res.put("message", "用户不在队伍中");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断自己是否是队长
        GroupsUserEntity groupsUserEntity = groupsUserEntityOptional.get();
        if (groupsUserEntity.getIsCreator() == Consts.GROUP_USER_IS_CREATOR) {
            res.put("code", 514402);
            res.put("message", "自己是队长");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 删除
        // 标记groupUser
        groupsUserEntity.setIsDel(Consts.IS_DEL);
        groupsUserEntity.setLeaveTime(commonUtil.getUnixTimestamp());
        groupsUserService.save(groupsUserEntity);
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/my")
    @ResponseBody
    public String my(HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUidOnlyNotDel(uid);
        if (!groupsUserEntityOptional.isPresent()) {
            res.put("code", 514501);
            res.put("message", "自己没有队伍");
            return new ObjectMapper().writeValueAsString(res);
        }
        GroupsUserEntity groupsUserEntity = groupsUserEntityOptional.get();
        GroupsEntity groupsEntity = groupsService.findById(groupsUserEntity.getGroupId()).get();
        List<GroupsUserEntity> groupsUserEntityList = groupsUserService.findAllByGroupIdAndIsDel(groupsUserEntity.getGroupId(), Consts.IS_NOT_DEL);
        resData.put("group_id", groupsEntity.getId());
        resData.put("group_name", groupsEntity.getName());
        resData.put("invite_code", groupsEntity.getInviteCode());
        List<Map<String, Object>> members = new ArrayList<>();
        resData.put("members", members);
        for (GroupsUserEntity groupsUser : groupsUserEntityList) {
            Map<String, Object> tmp = new HashMap<>();
            members.add(tmp);
            UsersEntity usersEntity = usersService.findById(groupsUser.getUid()).get();
            StudentInfoEntity studentInfoEntity = studentInfoService.findByUid(usersEntity.getUid()).get();
            tmp.put("student_id", studentInfoEntity.getStudentid());
            tmp.put("student_name", usersEntity.getName());
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @PostMapping("/transfer")
    @ResponseBody
    public String transfer(@RequestBody Map<String, String> requestBody, HttpSession session) throws JsonProcessingException {
        String student_id = requestBody.get("student_id");
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 判断自己是否在队伍中
        Optional<GroupsUserEntity> groupsUserEntityOptional = groupsUserService.findByUidOnlyNotDel(uid);
        if (!groupsUserEntityOptional.isPresent()) {
            res.put("code", 514601);
            res.put("message", "自己不处于队伍");
            return new ObjectMapper().writeValueAsString(res);
        }
        GroupsUserEntity groupsUserEntity = groupsUserEntityOptional.get();
        // 判断自己是否是队长
        if (groupsUserEntity.getIsCreator() == Consts.GROUP_USER_IS_NOT_CREATOR) {
            res.put("code", 514602);
            res.put("message", "自己并非队长");
            return new ObjectMapper().writeValueAsString(res);
        }
        // student_id是学号，需要先转化成uid
        Optional<StudentInfoEntity> targetStudentInfo = studentInfoService.findByStudentId(student_id);
        if (!targetStudentInfo.isPresent()) {
            res.put("code", 514603);
            res.put("message", "转让目标不存在");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断转让目标是否是队友
        Optional<GroupsUserEntity> targetUserOptional = groupsUserService.findByUidOnlyNotDel(targetStudentInfo.get().getUid());
        if (!targetUserOptional.isPresent()) {
            res.put("code", 514603);
            res.put("message", "转让目标不在队伍中");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 判断转让目标是否是队友
        GroupsUserEntity targetUser = targetUserOptional.get();
        if (!Objects.equals(targetUser.getGroupId(), groupsUserEntity.getGroupId())) {
            res.put("code", 514604);
            res.put("message", "队伍不相同");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 交换是否为为队长
        groupsUserEntity.setIsCreator(Consts.GROUP_USER_IS_NOT_CREATOR);
        targetUser.setIsCreator(Consts.GROUP_USER_IS_CREATOR);
        groupsUserService.save(groupsUserEntity);
        groupsUserService.save(targetUser);
        return new ObjectMapper().writeValueAsString(res);
    }
}
