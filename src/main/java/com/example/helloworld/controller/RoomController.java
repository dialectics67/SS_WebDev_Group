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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/room")
    @ResponseBody
    public String room(@RequestParam("id") Integer roomId, HttpSession session) throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        res.put("data", resData);
        res.put("code", 200);
        res.put("message", "操作成功");
        Optional<RoomsEntity> optionalRoomsEntity = roomsService.findById(roomId);
        Integer uid = (Integer) session.getAttribute(Consts.SESSION_USERS_UID);
        UsersEntity userEntity = usersService.findById(uid).get();
        Optional<BedsEntity> bedsEntityOptional = bedsService.findByUidAndStatusAndIsValidAndIsDel(uid, Consts.BED_STATUS_USED, Consts.IS_VALID, Consts.IS_NOT_DEL);
        if (!bedsEntityOptional.isPresent()) {
            res.put("code", 513304);
            res.put("message", "自己不是该房间的user");
        } else {
            Integer userUsedRoomId = bedsEntityOptional.get().getRoomId();
            if (optionalRoomsEntity.isPresent()) {
                RoomsEntity roomsEntity = optionalRoomsEntity.get();
                if (!Objects.equals(roomsEntity.getId(), userUsedRoomId)) {
                    res.put("code", 513304);
                    res.put("message", "自己不是该房间的user");
                } else if (roomsEntity.getIsDel() == Consts.IS_DEL) {
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
        }
        return new ObjectMapper().writeValueAsString(res);
    }

    @GetMapping("/empty")
    @ResponseBody
    public String empty() throws JsonProcessingException {
        // 最终返回的对象
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resData = new HashMap<>();
        List<Map<String, Integer>> row = new ArrayList<>();
        res.put("code", 200);
        res.put("message", "操作成功");
        res.put("data", resData);
        resData.put("row", row);

        // 获取有效的空床位
        List<BedsEntity> bedsEntityList = bedsService.findAll(Consts.BED_STATUS_EMPTY, Consts.IS_VALID, Consts.IS_NOT_DEL);
        Map<Integer, Integer> roomId2BedCount = new HashMap<>();
        // 遍历床位统计 房间->床位数量
        for (BedsEntity bedsEntity : bedsEntityList) {
            roomId2BedCount.put(bedsEntity.getRoomId(), roomId2BedCount.getOrDefault(bedsEntity.getRoomId(), 0) + 1);
        }

        // 获取有效的房间
        List<RoomsEntity> roomsEntityList = roomsService.findAllByIsValidAndIsDelOrderByOrderNum(Consts.IS_VALID, Consts.IS_NOT_DEL);
        // 统计 building->bedCount
        Map<Integer, Integer> maleBuildingId2BedCount = new HashMap<>();
        Map<Integer, Integer> femaleBuildingId2BedCount = new HashMap<>();
        for (RoomsEntity roomsEntity : roomsEntityList) {
            Integer itRoomBedCount = roomId2BedCount.get(roomsEntity.getId());
            if (roomsEntity.getGender() == Consts.GENDER_FEMALE) {
                femaleBuildingId2BedCount.put(roomsEntity.getBuildingId(), femaleBuildingId2BedCount.getOrDefault(roomsEntity.getBuildingId(), 0) + itRoomBedCount);
            } else {
                maleBuildingId2BedCount.put(roomsEntity.getBuildingId(), maleBuildingId2BedCount.getOrDefault(roomsEntity.getBuildingId(), 0) + itRoomBedCount);
            }
        }

        // 获取有效的building
        List<BuildingsEntity> buildingsEntityList = buildingsService.findAllByIsValidAndIsDelOrderByOrderNum(Consts.IS_VALID, Consts.IS_NOT_DEL);
        // 统计有效building的male bed count, female bed count
        for (BuildingsEntity buildingsEntity : buildingsEntityList) {
            Integer maleBedCount = maleBuildingId2BedCount.getOrDefault(buildingsEntity.getId(), 0);
            Integer femaleBedCount = femaleBuildingId2BedCount.getOrDefault(buildingsEntity.getId(), 0);

            Map<String, Integer> tmp = new HashMap<>();
            tmp.put("building_id", buildingsEntity.getId());
            tmp.put("gender", Consts.GENDER_MALE);
            tmp.put("cnt", maleBedCount);
            row.add(tmp);
            Map<String, Integer> tmp1 = new HashMap<>();
            tmp1.put("building_id", buildingsEntity.getId());
            tmp1.put("gender", Consts.GENDER_FEMALE);
            tmp1.put("cnt", femaleBedCount);
            row.add(tmp1);
        }
        return new ObjectMapper().writeValueAsString(res);
    }
}
