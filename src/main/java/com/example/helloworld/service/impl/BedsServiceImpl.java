package com.example.helloworld.service.impl;


import com.example.helloworld.entity.BedsEntity;
import com.example.helloworld.repository.BedsRepository;
import com.example.helloworld.service.BedsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private BedsRepository bedsRepository;

    @Override
    public void save(BedsEntity beds) {
        bedsRepository.save(beds);
    }

    @Override
    public void deleteById(Integer id) {
        bedsRepository.deleteById(id);
    }

    @Override
    public List<BedsEntity> findAll(Integer status, Integer is_valid, Integer is_del) {
        return bedsRepository.findAllByStatusAndIsValidAndIsDelOrderByOrderNum(status, is_valid, is_del);
    }

    @Override
    public Optional<BedsEntity> findById(Integer id) {
        return bedsRepository.findById(id);
    }

    @Override
    public List<BedsEntity> findAllByRoomId(Integer roomId, Integer status, Integer is_valid, Integer is_del) {
        return bedsRepository.findAllByRoomIdAndStatusAndIsValidAndIsDelOrderByOrderNum(roomId, status, is_valid, is_del);
    }

    @Override
    public Optional<BedsEntity> findByUidAndStatusAndIsValidAndIsDel(Integer uid, Integer status, Integer is_valid, Integer is_del) {
        return bedsRepository.findByUidAndStatusAndIsValidAndIsDel(uid, status, is_valid, is_del);
    }

    @Override
    public Integer countByRoomIdAndStatusAndIsValidAndIsDel(Integer roomId, Integer status, Integer isValid, Integer isDel) {
        return bedsRepository.findAllByRoomIdAndStatusAndIsValidAndIsDelOrderByOrderNum(roomId, status, isValid, isDel).size();
    }
}

