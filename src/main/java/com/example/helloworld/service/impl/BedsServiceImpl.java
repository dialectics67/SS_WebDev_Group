package com.example.helloworld.service.impl;


import com.example.helloworld.entity.BedsEntity;
import com.example.helloworld.repository.BedsRepository;
import com.example.helloworld.repository.BuildingsRepository;
import com.example.helloworld.repository.RoomsRepository;
import com.example.helloworld.service.BedsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:04
 */
@Service
public class BedsServiceImpl implements BedsService {
    @Resource
    private BuildingsRepository buildingsRepository;
    @Resource
    private BedsRepository bedsRepository;

    @Resource
    private RoomsRepository roomsRepository;

    @Override
    public void save(BedsEntity beds) {
        bedsRepository.save(beds);
    }

    @Override
    public void deleteById(Integer id) {
        bedsRepository.deleteById(id);
    }

    @Override
    public List<BedsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer is_valid, Integer is_del) {
        return bedsRepository.findAllByIsValidAndIsDelOrderByOrderNum(is_valid, is_del);
    }

    @Override
    public Optional<BedsEntity> findById(Integer id) {
        return bedsRepository.findById(id);
    }

    @Override
    public List<BedsEntity> findByRoomId(Collection<Integer> roomIds, Integer is_valid, Integer is_del) {
        return bedsRepository.findAllByRoomIdInAndIsValidAndIsDel(roomIds, is_valid, is_del);
    }
}

