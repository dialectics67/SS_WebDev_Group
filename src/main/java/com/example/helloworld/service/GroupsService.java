package com.example.helloworld.service;


import com.example.helloworld.entity.GroupsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface GroupsService {

    void save(GroupsEntity groups);

    void deleteById(Integer id);

    Optional<GroupsEntity> findById(Integer id);

    List<GroupsEntity> findById(Collection<Integer> ids);

}

