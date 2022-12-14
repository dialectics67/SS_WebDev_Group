package com.example.helloworld.repository;

import com.example.helloworld.entity.StudentInfoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentInfoRepository extends CrudRepository<StudentInfoEntity, Integer> {

    Optional<StudentInfoEntity> findByUid(Integer uid);

    Optional<StudentInfoEntity> findByStudentid(String studentid);
}
