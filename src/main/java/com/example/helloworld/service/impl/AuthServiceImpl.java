package com.example.helloworld.service.impl;


import com.example.helloworld.entity.AuthEntity;
import com.example.helloworld.repository.AuthRepository;
import com.example.helloworld.service.AuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:04
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AuthRepository authRepository;

    @Override
    public void save(AuthEntity auth) {
        authRepository.save(auth);
    }

    @Override
    public void deleteById(Integer id) {
        authRepository.deleteById(id);
    }

    @Override
    public Optional<AuthEntity> findById(Integer id) {
        return authRepository.findById(id);
    }

    @Override
    public Optional<AuthEntity> findByUsernameAndType(String username, Integer type) {
        return authRepository.findByUsernameAndType(username, type);
    }

    @Override
    public Optional<AuthEntity> findByUidAndType(Integer uid, Integer type) {
        return authRepository.findByUidAndType(uid, type);
    }

    @Override
    public List<AuthEntity> findById(Collection<Integer> ids) {
        Iterable<AuthEntity> iterable = authRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

