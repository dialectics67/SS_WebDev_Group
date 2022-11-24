package com.example.helloworld.service.impl;


import com.example.helloworld.entity.LogsEntity;
import com.example.helloworld.repository.LogsRepository;
import com.example.helloworld.service.LogsService;
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
public class LogsServiceImpl implements LogsService {

    @Resource
    private LogsRepository logsRepository;

    @Override
    public void save(LogsEntity logs) {
        logsRepository.save(logs);
    }

    @Override
    public void deleteById(Integer id) {
        logsRepository.deleteById(id);
    }

    @Override
    public Optional<LogsEntity> findById(Integer id) {
        return logsRepository.findById(id);
    }

    @Override
    public List<LogsEntity> findById(Collection<Integer> ids) {
        Iterable<LogsEntity> iterable = logsRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

