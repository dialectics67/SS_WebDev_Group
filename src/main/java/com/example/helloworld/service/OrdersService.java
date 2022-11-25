package com.example.helloworld.service;


import com.example.helloworld.entity.OrdersEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
public interface OrdersService {

    void save(OrdersEntity orders);

    void deleteById(Integer id);

    Optional<OrdersEntity> findById(Integer id);

    List<OrdersEntity> findById(Collection<Integer> ids);

    List<OrdersEntity> findAllByUid(Integer uid);
}

