package com.example.helloworld.service.impl;


import com.example.helloworld.entity.UsersEntity;
import com.example.helloworld.repository.UsersRepository;
import com.example.helloworld.service.UsersService;
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
 * @since 2022-11-22 12:04:05
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersRepository usersRepository;

    @Override
    public void save(UsersEntity users) {
        usersRepository.save(users);
    }

    @Override
    public void deleteById(Integer id) {
        usersRepository.deleteById(id);
    }

    @Override
    public Optional<UsersEntity> findById(Integer id) {
        return usersRepository.findById(id);
    }

    @Override
    public List<UsersEntity> findById(Collection<Integer> ids) {
        Iterable<UsersEntity> iterable = usersRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

