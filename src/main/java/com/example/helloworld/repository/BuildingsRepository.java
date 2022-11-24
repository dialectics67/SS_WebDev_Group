package com.example.helloworld.repository;

import com.example.helloworld.entity.BuildingsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingsRepository extends CrudRepository<BuildingsEntity, Integer> {
}
