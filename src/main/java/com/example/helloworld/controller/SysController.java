package com.example.helloworld.controller;

import com.example.helloworld.service.SysService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sys")
public class SysController {
    private final SysService sysService;

    public SysController(SysService sysService) {
        this.sysService = sysService;
    }

    @GetMapping("/opentime")
    @ResponseBody
    public String opentime() throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        try {
            resData.put("start_time", sysService.findByKeyName("start_time").get().getKeyValue());
            resData.put("end_time", sysService.findByKeyName("end_time").get().getKeyValue());
        } catch (Exception e) {
            res.put("code", 510000);
            res.put("message", e.getMessage());
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/classlimit")
    @ResponseBody
    public String classlimit() throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        try {
            resData.put("class_limit", sysService.findByKeyName("class_limit").get().getKeyValue());
        } catch (Exception e) {
            res.put("code", 510000);
            res.put("message", e.getMessage());
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/groupnum")
    @ResponseBody
    public String groupnum() throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        try {
            resData.put("group_limit", sysService.findByKeyName("group_limit").get().getKeyValue());
            resData.put("group_num", sysService.findByKeyName("group_num").get().getKeyValue());
        } catch (Exception e) {
            res.put("code", 510000);
            res.put("message", e.getMessage());
        }
        return new ObjectMapper().writeValueAsString(res);
    }
}
