package com.example.helloworld.repository;

import com.example.helloworld.entity.RoomsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsRepository extends CrudRepository<RoomsEntity, Integer> {
}
