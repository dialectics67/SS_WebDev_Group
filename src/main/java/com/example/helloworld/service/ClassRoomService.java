package com.example.helloworld.service;


import com.example.helloworld.entity.ClassRoomEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:04
 */
public interface ClassRoomService {

    void save(ClassRoomEntity classRoom);

    void deleteById(Integer id);

    Optional<ClassRoomEntity> findById(Integer id);

    List<ClassRoomEntity> findById(Collection<Integer> ids);

}

