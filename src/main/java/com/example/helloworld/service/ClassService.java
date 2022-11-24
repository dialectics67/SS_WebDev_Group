package com.example.helloworld.service;


import com.example.helloworld.entity.ClassEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:04
 */
public interface ClassService {

    void save(ClassEntity classEntity);

    void deleteById(Integer id);

    Optional<ClassEntity> findById(Integer id);

    List<ClassEntity> findById(Collection<Integer> ids);

}

