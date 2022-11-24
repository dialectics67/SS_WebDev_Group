package com.example.helloworld.repository;

import com.example.helloworld.entity.BedsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BedsRepository extends CrudRepository<BedsEntity, Integer> {
}
