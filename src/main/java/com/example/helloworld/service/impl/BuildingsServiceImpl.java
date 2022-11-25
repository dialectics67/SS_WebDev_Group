package com.example.helloworld.service.impl;


import com.example.helloworld.entity.BuildingsEntity;
import com.example.helloworld.repository.BuildingsRepository;
import com.example.helloworld.service.BuildingsService;
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
 * @since 2022-11-22 12:04:04
 */
@Service
public class BuildingsServiceImpl implements BuildingsService {

    @Resource
    private BuildingsRepository buildingsRepository;

    @Override
    public void save(BuildingsEntity buildings) {
        buildingsRepository.save(buildings);
    }

    @Override
    public void deleteById(Integer id) {
        buildingsRepository.deleteById(id);
    }

    @Override
    public List<BuildingsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer isValid, Integer isDel) {
        return buildingsRepository.findAllByIsValidAndIsDelOrderByOrderNum(isValid, isDel);
    }

    @Override
    public Optional<BuildingsEntity> findById(Integer id) {
        return buildingsRepository.findById(id);
    }

    @Override
    public List<BuildingsEntity> findById(Collection<Integer> ids, Integer is_valid, Integer is_del) {
        Iterable<BuildingsEntity> iterable = buildingsRepository.findByIdInAndIsValidAndIsDelOrderByOrderNum(ids, is_valid, is_del);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

