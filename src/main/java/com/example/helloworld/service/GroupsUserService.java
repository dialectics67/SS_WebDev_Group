package com.example.helloworld.service;


import com.example.helloworld.entity.GroupsUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface GroupsUserService {

    void save(GroupsUserEntity groupsUser);

    void deleteById(Integer id);

    Optional<GroupsUserEntity> findById(Integer id);

    List<GroupsUserEntity> findById(Collection<Integer> ids);

    List<GroupsUserEntity> findByUid(Integer uid);

    Optional<GroupsUserEntity> findByUidOnlyNotDel(Integer uid);

    List<GroupsUserEntity> findAllByUidAndGroupId(Integer uid, Integer groupId);

    List<GroupsUserEntity> findAllByGroupIdAndIsDel(Integer group_id, Integer is_del);
}

