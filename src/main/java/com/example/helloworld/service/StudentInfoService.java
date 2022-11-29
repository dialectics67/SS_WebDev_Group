package com.example.helloworld.service;


import com.example.helloworld.entity.StudentInfoEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface StudentInfoService {

    void save(StudentInfoEntity studentInfo);

    void deleteById(Integer id);

    Optional<StudentInfoEntity> findById(Integer id);

    Optional<StudentInfoEntity> findByUid(Integer uid);

    List<StudentInfoEntity> findById(Collection<Integer> ids);

}

