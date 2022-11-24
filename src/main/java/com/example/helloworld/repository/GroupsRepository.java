package com.example.helloworld.repository;

import com.example.helloworld.entity.GroupsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepository extends CrudRepository<GroupsEntity, Integer> {
}
