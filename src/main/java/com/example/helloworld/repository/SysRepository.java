package com.example.helloworld.repository;

import com.example.helloworld.entity.SysEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRepository extends CrudRepository<SysEntity, Integer> {
}
