package com.example.helloworld.service;


import com.example.helloworld.entity.BedsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:04
 */
public interface BedsService {

    void save(BedsEntity beds);

    void deleteById(Integer id);

    List<BedsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer is_valid, Integer is_del);

    Optional<BedsEntity> findById(Integer id);

    List<BedsEntity> findByRoomId(Collection<Integer> ids, Integer is_valid, Integer is_del);

}

