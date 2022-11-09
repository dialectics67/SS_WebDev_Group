package com.example.helloworld.service;


import com.example.helloworld.entity.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * user basic information业务层
 *
 * @author dialectics67
 * @since 2022-10-15 19:07:19
 */
public interface UserService {

    void save(UserEntity user);

    void deleteById(Long id);

    Iterable<UserEntity> findAll();

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUserId(Long userId);

    List<UserEntity> findById(Collection<Long> ids);

//    Page<UserEntity> list(Pageable page);

}

