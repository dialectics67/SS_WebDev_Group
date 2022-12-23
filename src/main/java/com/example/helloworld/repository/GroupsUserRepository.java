package com.example.helloworld.repository;

import com.example.helloworld.entity.GroupsUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupsUserRepository extends CrudRepository<GroupsUserEntity, Integer> {
    List<GroupsUserEntity> findAllByGroupIdAndIsDel(Integer group_id, Integer is_del);

    Optional<GroupsUserEntity> findByUidAndIsDel(Integer uid, Integer is_del);

    List<GroupsUserEntity> findAllByUidAndGroupId(Integer uid, Integer groupId);

    List<GroupsUserEntity> findAllByUid(Integer uid);
}
