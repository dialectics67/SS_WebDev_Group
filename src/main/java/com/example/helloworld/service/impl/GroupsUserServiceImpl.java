package com.example.helloworld.service.impl;


import com.example.helloworld.constants.Consts;
import com.example.helloworld.entity.GroupsUserEntity;
import com.example.helloworld.repository.GroupsUserRepository;
import com.example.helloworld.service.GroupsUserService;
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
public class GroupsUserServiceImpl implements GroupsUserService {

    @Resource
    private GroupsUserRepository groupsUserRepository;

    @Override
    public void save(GroupsUserEntity groupsUser) {
        groupsUserRepository.save(groupsUser);
    }

    @Override
    public void deleteById(Integer id) {
        groupsUserRepository.deleteById(id);
    }

    @Override
    public Optional<GroupsUserEntity> findById(Integer id) {
        return groupsUserRepository.findById(id);
    }

    @Override
    public List<GroupsUserEntity> findById(Collection<Integer> ids) {
        Iterable<GroupsUserEntity> iterable = groupsUserRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupsUserEntity> findByUid(Integer uid) {
        return groupsUserRepository.findAllByUid(uid);
    }

    @Override
    public Optional<GroupsUserEntity> findByUidOnlyNotDel(Integer uid) {
        return groupsUserRepository.findByUidAndIsDel(uid, Consts.IS_NOT_DEL);
    }

    @Override
    public List<GroupsUserEntity> findAllByUidAndGroupId(Integer uid, Integer groupId) {
        return groupsUserRepository.findAllByUidAndGroupId(uid, groupId);
    }

    @Override
    public List<GroupsUserEntity> findAllByGroupIdAndIsDel(Integer group_id, Integer is_del) {
        return groupsUserRepository.findAllByGroupIdAndIsDel(group_id, is_del);
    }

}

