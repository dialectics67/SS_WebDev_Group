package com.example.helloworld.service;


import com.example.helloworld.entity.RoomsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface RoomsService {

    void save(RoomsEntity rooms);

    void deleteById(Integer id);

    List<RoomsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer is_valid, Integer is_del);

    List<RoomsEntity> findAllByGender(Integer gender, Integer is_valid, Integer is_del);

    List<RoomsEntity> findAllByBuildingIdAndGender(Collection<Integer> buildingIds, Integer gender, Integer is_valid, Integer is_del);

    Optional<RoomsEntity> findById(Integer id);

    List<RoomsEntity> findById(Collection<Integer> ids);

    // 查找所有含有指定性别房间的楼层
    Set<Integer> findAllBuildingIdByGender(Integer gender, Integer is_valid, Integer is_del);

    // 筛除所有不能住的宿舍


}

