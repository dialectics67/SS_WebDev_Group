package com.example.helloworld.service;


import com.example.helloworld.entity.RoomsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface RoomsService {

    void save(RoomsEntity rooms);

    void deleteById(Integer id);

    Optional<RoomsEntity> findById(Integer id);

    List<RoomsEntity> findById(Collection<Integer> ids);

}

