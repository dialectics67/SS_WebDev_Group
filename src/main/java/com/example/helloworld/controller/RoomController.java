package com.example.helloworld.controller;

import com.example.helloworld.constants.Consts;
import com.example.helloworld.entity.BedsEntity;
import com.example.helloworld.entity.BuildingsEntity;
import com.example.helloworld.entity.RoomsEntity;
import com.example.helloworld.entity.UsersEntity;
import com.example.helloworld.service.BedsService;
import com.example.helloworld.service.BuildingsService;
import com.example.helloworld.service.RoomsService;
import com.example.helloworld.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomsService roomsService;
    @Autowired
    private BuildingsService buildingsService;

    @Autowired
    private BedsService bedsService;

    @GetMapping("/buildinglist")
    @ResponseBody
    public String buildinglist(HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        UsersEntity userEntity = usersService.findById(uid).get();
        Integer userGender = userEntity.getGender();
        Set<Integer> buildingIdSet = roomsService.findAllBuildingIdByGender(userGender, Consts.IS_VALID, Consts.IS_NOT_DEL);

        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        List<BuildingsEntity> buildingsEntityList = buildingsService.findById(buildingIdSet, Consts.IS_VALID, Consts.IS_NOT_DEL);
        for (BuildingsEntity buildingsEntity : buildingsEntityList) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("building_id", buildingsEntity.getId());
            tmp.put("building_name", buildingsEntity.getName());
            rows.add(tmp);
        }
        resData.put("rows", rows);
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/building")
    @ResponseBody
    public String building(Integer id) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        Optional<BuildingsEntity> optionalBuildingsEntity = buildingsService.findById(id);
        if (optionalBuildingsEntity.isPresent()) {
            BuildingsEntity buildingsEntity = optionalBuildingsEntity.get();
            resData.put("name", buildingsEntity.getName());
            resData.put("describe", buildingsEntity.getDescribe());
            resData.put("image_url", buildingsEntity.getImageUrl());
        } else {
            res.put("code", 513201);
            res.put("message", "未查询到相应楼号");
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/room/{id}")
    @ResponseBody
    public String room(@PathVariable("id") Integer roomId) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        Optional<RoomsEntity> optionalRoomsEntity = roomsService.findById(roomId);
        if (optionalRoomsEntity.isPresent()) {
            RoomsEntity roomsEntity = optionalRoomsEntity.get();
            if (roomsEntity.getIsDel() == Consts.IS_DEL) {
                res.put("code", 513302);
                res.put("message", "房间已被删除");
            } else if (roomsEntity.getIsValid() == Consts.IS_NOT_VALID) {
                res.put("code", 513303);
                res.put("message", "房间不可用");
            } else {
                resData.put("name", roomsEntity.getName());
                resData.put("gender", roomsEntity.getGender());
                resData.put("describe", roomsEntity.getDescribe());
                resData.put("image_url", roomsEntity.getImageUrl());
                resData.put("building_id", roomsEntity.getBuildingId());
            }
        } else {
            res.put("code", 513301);
            res.put("message", "房间id不存在");
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/empty")
    @ResponseBody
    public String empty(Integer gender) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        List<Map<String, Integer>> row = new ArrayList<>();
        res.put("code", 200);
        res.put("message", "操作成功");
        res.put("data", resData);
        resData.put("row", row);
        if (gender != 0 && gender != 1) {
            res.put("code", 513401);
            res.put("message", "性别非法");
        } else {
            // 查找合法的building id, 可优化
            List<BuildingsEntity> buildingsEntityIterable = buildingsService.findAllByIsValidAndIsDelOrderByOrderNum(Consts.IS_VALID, Consts.IS_NOT_DEL);
            Set<Integer> buildingIdSet = new HashSet<>();
            for (BuildingsEntity buildingsEntity : buildingsEntityIterable) {
                buildingIdSet.add(buildingsEntity.getId());
            }
            // 查找合法的room id, 可优化
            List<RoomsEntity> roomsEntityIterable = roomsService.findAllByBuildingIdAndGender(buildingIdSet, gender, Consts.IS_VALID, Consts.IS_NOT_DEL);
            Set<Integer> roomIdSet = new HashSet<>();
            Map<Integer, Integer> roomId2BuildingId = new HashMap<>();
            for (RoomsEntity roomsEntity : roomsEntityIterable) {
                roomIdSet.add(roomsEntity.getId());
                roomId2BuildingId.put(roomsEntity.getId(), roomsEntity.getBuildingId());
            }
            // 分类所有的合法房间
            List<BedsEntity> bedsEntityIterable1 = bedsService.findByRoomId(roomIdSet, Consts.IS_VALID, Consts.IS_NOT_DEL);
            Map<Integer, Integer> buildingId2BedNum = new HashMap<>();
            for (BedsEntity bedsEntity : bedsEntityIterable1) {
                Integer buildingId = roomId2BuildingId.get(bedsEntity.getRoomId());
                Integer bedNum = buildingId2BedNum.get(buildingId);
                if (bedNum == null) {
                    bedNum = 0;
                }
                buildingId2BedNum.put(buildingId, bedNum + 1);
            }

            for (Map.Entry<Integer, Integer> entry : buildingId2BedNum.entrySet()) {
                Map<String, Integer> tmp = new HashMap<>();
                tmp.put("building_id", entry.getKey());
                tmp.put("gender", gender);
                tmp.put("cnt", entry.getValue());
                row.add(tmp);
            }
        }
        return new ObjectMapper().writeValueAsString(res);
    }
}
