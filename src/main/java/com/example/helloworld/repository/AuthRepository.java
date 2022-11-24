package com.example.helloworld.repository;

import com.example.helloworld.entity.AuthEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends CrudRepository<AuthEntity, Integer> {
     Optional<AuthEntity> findByUsernameAndType(String username, Integer type);

     Iterable<AuthEntity> findAllByUsernameAndType(String username, Integer type);
}
