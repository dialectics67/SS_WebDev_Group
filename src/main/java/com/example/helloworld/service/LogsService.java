package com.example.helloworld.service;


import com.example.helloworld.entity.LogsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface LogsService {

    void save(LogsEntity logs);

    void deleteById(Integer id);

    Optional<LogsEntity> findById(Integer id);

    List<LogsEntity> findById(Collection<Integer> ids);

}

