package com.example.helloworld.repository;

import com.example.helloworld.entity.ClassRoomEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends CrudRepository<ClassRoomEntity, Integer> {
}
