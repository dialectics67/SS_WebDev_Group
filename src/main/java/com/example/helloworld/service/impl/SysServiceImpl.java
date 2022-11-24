package com.example.helloworld.service.impl;


import com.example.helloworld.entity.SysEntity;
import com.example.helloworld.repository.SysRepository;
import com.example.helloworld.service.SysService;
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
public class SysServiceImpl implements SysService {

    @Resource
    private SysRepository sysRepository;

    @Override
    public void save(SysEntity sys) {
        sysRepository.save(sys);
    }

    @Override
    public void deleteById(Integer id) {
        sysRepository.deleteById(id);
    }

    @Override
    public Optional<SysEntity> findById(Integer id) {
        return sysRepository.findById(id);
    }

    @Override
    public List<SysEntity> findById(Collection<Integer> ids) {
        Iterable<SysEntity> iterable = sysRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

