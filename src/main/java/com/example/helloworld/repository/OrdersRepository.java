package com.example.helloworld.repository;

import com.example.helloworld.entity.OrdersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends CrudRepository<OrdersEntity, Integer> {
    List<OrdersEntity> findAllByUid(Integer uid);
}
