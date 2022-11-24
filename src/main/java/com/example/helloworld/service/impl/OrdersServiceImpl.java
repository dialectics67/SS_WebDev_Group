package com.example.helloworld.service.impl;


import com.example.helloworld.entity.OrdersEntity;
import com.example.helloworld.repository.OrdersRepository;
import com.example.helloworld.service.OrdersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:05
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    @Resource
    private OrdersRepository ordersRepository;

    @Override
    public void save(OrdersEntity orders) {
        ordersRepository.save(orders);
    }

    @Override
    public void deleteById(Integer id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public Optional<OrdersEntity> findById(Integer id) {
        return ordersRepository.findById(id);
    }

    @Override
    public List<OrdersEntity> findById(Collection<Integer> ids) {
        Iterable<OrdersEntity> iterable = ordersRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

