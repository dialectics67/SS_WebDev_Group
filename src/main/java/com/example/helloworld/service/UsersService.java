package com.example.helloworld.service;


import com.example.helloworld.entity.UsersEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface UsersService {

    void save(UsersEntity users);

    void deleteById(Integer id);

    Optional<UsersEntity> findById(Integer id);

    List<UsersEntity> findById(Collection<Integer> ids);

}

