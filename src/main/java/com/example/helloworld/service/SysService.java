package com.example.helloworld.service;


import com.example.helloworld.entity.SysEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface SysService {

    void save(SysEntity sys);

    void deleteById(Integer id);

    Optional<SysEntity> findById(Integer id);

    List<SysEntity> findById(Collection<Integer> ids);

}

