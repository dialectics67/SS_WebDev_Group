package com.example.helloworld.service.impl;


import com.example.helloworld.entity.ClassEntity;
import com.example.helloworld.repository.ClassRepository;
import com.example.helloworld.service.ClassService;
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
public class ClassServiceImpl implements ClassService {

    @Resource
    private ClassRepository classRepository;

    @Override
    public void save(ClassEntity classEntity) {
        classRepository.save(classEntity);
    }

    @Override
    public void deleteById(Integer id) {
        classRepository.deleteById(id);
    }

    @Override
    public Optional<ClassEntity> findById(Integer id) {
        return classRepository.findById(id);
    }

    @Override
    public List<ClassEntity> findById(Collection<Integer> ids) {
        Iterable<ClassEntity> iterable = classRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

