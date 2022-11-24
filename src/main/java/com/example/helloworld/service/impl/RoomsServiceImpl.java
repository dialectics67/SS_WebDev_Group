package com.example.helloworld.service.impl;


import com.example.helloworld.entity.RoomsEntity;
import com.example.helloworld.repository.RoomsRepository;
import com.example.helloworld.service.RoomsService;
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
public class RoomsServiceImpl implements RoomsService {

    @Resource
    private RoomsRepository roomsRepository;

    @Override
    public void save(RoomsEntity rooms) {
        roomsRepository.save(rooms);
    }

    @Override
    public void deleteById(Integer id) {
        roomsRepository.deleteById(id);
    }

    @Override
    public Optional<RoomsEntity> findById(Integer id) {
        return roomsRepository.findById(id);
    }

    @Override
    public List<RoomsEntity> findById(Collection<Integer> ids) {
        Iterable<RoomsEntity> iterable = roomsRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

