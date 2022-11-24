package com.example.helloworld.repository;

import com.example.helloworld.entity.GroupsUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsUserRepository extends CrudRepository<GroupsUserEntity, Integer> {
}
