package com.example.helloworld.repository;

import com.example.helloworld.entity.StudentInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentInfoRepository extends CrudRepository<StudentInfoEntity, Integer> {
}
