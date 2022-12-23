package com.example.helloworld.service.impl;


import com.example.helloworld.entity.RoomsEntity;
import com.example.helloworld.repository.RoomsRepository;
import com.example.helloworld.service.RoomsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
@Service
public class RoomsServiceImpl implements RoomsService {

    @Resource
    private RoomsRepository roomsRepository;

    @Override
    public void save(RoomsEntity rooms) {
        roomsRepository.save(rooms);
    }

    @Override
    public void deleteById(Integer id) {
        roomsRepository.deleteById(id);
    }

    @Override
    public List<RoomsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer is_valid, Integer is_del) {
        return roomsRepository.findAllByIsValidAndIsDelOrderByOrderNum(is_valid, is_del);
    }

    @Override
    public List<RoomsEntity> findAllByGender(Integer gender, Integer is_valid, Integer is_del) {
        return roomsRepository.findAllByGenderAndIsValidAndIsDelOrderByOrderNum(gender, is_valid, is_del);
    }


    @Override
    public Optional<RoomsEntity> findById(Integer id) {
        return roomsRepository.findById(id);
    }

    @Override
    public List<RoomsEntity> findById(Collection<Integer> ids) {
        Iterable<RoomsEntity> iterable = roomsRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Integer> findAllBuildingIdByGender(Integer gender, Integer is_valid, Integer is_del) {
        // 根据性别查找所有房间
        List<RoomsEntity> roomEntityList = this.findAllByGender(gender, is_valid, is_del);
        // 统计楼号
        Set<Integer> buildingIdSet = new HashSet<>();
        for (RoomsEntity roomEntity : roomEntityList) {
            buildingIdSet.add(roomEntity.getBuildingId());
        }
        return buildingIdSet;
    }

    @Override
    public List<RoomsEntity> findAllByBuildingIdAndGenderAndIsValidAndIsDel(Integer buildingId, Integer gender, Integer isValid, Integer isDel) {
        return roomsRepository.findAllByBuildingIdAndGenderAndIsValidAndIsDelOrderByOrderNum(buildingId, gender, isValid, isDel);
    }

}

