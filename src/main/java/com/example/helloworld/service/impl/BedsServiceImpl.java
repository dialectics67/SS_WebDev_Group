package com.example.helloworld.service.impl;


import com.example.helloworld.entity.BedsEntity;
import com.example.helloworld.repository.BedsRepository;
import com.example.helloworld.service.BedsService;
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
    public Optional<BedsEntity> findById(Integer id) {
        return bedsRepository.findById(id);
    }

    @Override
    public List<BedsEntity> findById(Collection<Integer> ids) {
        Iterable<BedsEntity> iterable = bedsRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

