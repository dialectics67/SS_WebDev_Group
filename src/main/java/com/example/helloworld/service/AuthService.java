package com.example.helloworld.service;


import com.example.helloworld.entity.AuthEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:04
 */
public interface AuthService {

    void save(AuthEntity auth);

    void deleteById(Integer id);

    Optional<AuthEntity> findById(Integer id);

    Optional<AuthEntity> findByUsernameAndType(String username, Integer type);

    Optional<AuthEntity> findByUidAndType(Integer uid, Integer type);

    List<AuthEntity> findById(Collection<Integer> ids);

}

