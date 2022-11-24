package com.example.helloworld.repository;

import com.example.helloworld.entity.LogsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends CrudRepository<LogsEntity, Integer> {
}
