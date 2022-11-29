package com.example.helloworld.service.impl;


import com.example.helloworld.entity.GroupsEntity;
import com.example.helloworld.repository.GroupsRepository;
import com.example.helloworld.service.GroupsService;
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
public class GroupsServiceImpl implements GroupsService {

    @Resource
    private GroupsRepository groupsRepository;

    @Override
    public void save(GroupsEntity groups) {
        groupsRepository.save(groups);
    }

    @Override
    public void deleteById(Integer id) {
        groupsRepository.deleteById(id);
    }

    @Override
    public Optional<GroupsEntity> findById(Integer id) {
        return groupsRepository.findById(id);
    }

    @Override
    public Optional<GroupsEntity> findByInviteCode(String inviteCode) {
        return groupsRepository.findByInviteCode(inviteCode);
    }

    @Override
    public List<GroupsEntity> findById(Collection<Integer> ids) {
        Iterable<GroupsEntity> iterable = groupsRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

