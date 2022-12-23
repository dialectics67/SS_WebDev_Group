package com.example.helloworld.controller;

import com.example.helloworld.constants.Consts;
import com.example.helloworld.entity.*;
import com.example.helloworld.service.*;
import com.example.helloworld.utils.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private ClassService classService;
    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private BedsService bedsService;
    @Autowired
    private RoomsService roomsService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private AuthService authService;

    @GetMapping("/myinfo")
    @ResponseBody
    public String myinfo(HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");

        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        UsersEntity usersEntity = usersService.findById(uid).get();
        StudentInfoEntity studentInfoEntity = studentInfoService.findByUid(uid).get();
        ClassEntity classEntity = classService.findById(studentInfoEntity.getClassId()).get();
        resData.put("uid", usersEntity.getUid());
        resData.put("studentid", studentInfoEntity.getStudentid());
        resData.put("name", usersEntity.getName());
        resData.put("gender", usersEntity.getGender());
        resData.put("email", usersEntity.getEmail());
        resData.put("tel", usersEntity.getTel());
        resData.put("last_login_time", usersEntity.getLastLoginTime());
        resData.put("verification_code", studentInfoEntity.getVerificationCode());
        resData.put("class_name", classEntity.getName());
        return new ObjectMapper().writeValueAsString(res);
    }

    @PostMapping("/passwd")
    @ResponseBody
    public String passwd(@RequestBody Map<String, String> requestBody, HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");

        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        // 获取提交
        String subOldPassword = requestBody.get("oldPasswd");
        String subNewPassword = requestBody.get("newPasswd");
        // 获取原始密码
        AuthEntity authEntity = authService.findByUidAndType(uid, 0).get();
        String oldPassword = authEntity.getPassword();
        // 判断密码是否相等
        if (!Objects.equals(commonUtil.getMD5(subOldPassword), oldPassword)) {
            res.put("code", 512201);
            res.put("message", "旧密码错误");
            return new ObjectMapper().writeValueAsString(res);
        }
        // 写回密码
        authEntity.setPassword(commonUtil.getMD5(subNewPassword));
        authService.save(authEntity);
        // 强制登出

        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/myroom")
    @ResponseBody
    public String myroom(HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");

        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        Optional<BedsEntity> bedsEntityOptional = bedsService.findByUidAndStatusAndIsValidAndIsDel(uid, Consts.BED_STATUS_USED, Consts.IS_VALID, Consts.IS_NOT_DEL);
        if (!bedsEntityOptional.isPresent()) {
            res.put("code", 512301);
            res.put("message", "自己并没有宿舍");
            return new ObjectMapper().writeValueAsString(res);
        }
        BedsEntity bedsEntity = bedsEntityOptional.get();
        RoomsEntity roomsEntity = roomsService.findById(bedsEntity.getRoomId()).get();
        resData.put("roomName", roomsEntity.getName());
        resData.put("roomId", roomsEntity.getId());
        List<Map<String, Object>> members = new ArrayList<>();
        // 查找队友
        List<BedsEntity> bedsEntityList = bedsService.findAllByRoomId(roomsEntity.getId(), Consts.BED_STATUS_USED, Consts.IS_VALID, Consts.IS_NOT_DEL);
        for (BedsEntity bedsEntity1 : bedsEntityList) {
            UsersEntity usersEntity = usersService.findById(bedsEntity1.getUid()).get();
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("name", usersEntity.getName());
            members.add(tmp);
        }
        resData.put("member", members);
        return new ObjectMapper().writeValueAsString(res);
    }
}
