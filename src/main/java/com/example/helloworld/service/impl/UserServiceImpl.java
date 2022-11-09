package com.example.helloworld.service.impl;


import com.example.helloworld.entity.UserEntity;
import com.example.helloworld.repository.UserRepository;
import com.example.helloworld.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * user basic information业务层
 *
 * @author dialectics67
 * @since 2022-10-15 19:07:19
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<UserEntity> findById(Collection<Long> ids) {
        Iterable<UserEntity> iterable = userRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}

