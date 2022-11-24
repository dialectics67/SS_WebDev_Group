package com.example.helloworld.service.impl;


import com.example.helloworld.entity.ClassRoomEntity;
import com.example.helloworld.repository.ClassRoomRepository;
import com.example.helloworld.service.ClassRoomService;
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
public class ClassRoomServiceImpl implements ClassRoomService {

    @Resource
    private ClassRoomRepository classRoomRepository;

    @Override
    public void save(ClassRoomEntity classRoom) {
        classRoomRepository.save(classRoom);
    }

    @Override
    public void deleteById(Integer id) {
        classRoomRepository.deleteById(id);
    }

    @Override
    public Optional<ClassRoomEntity> findById(Integer id) {
        return classRoomRepository.findById(id);
    }

    @Override
    public List<ClassRoomEntity> findById(Collection<Integer> ids) {
        Iterable<ClassRoomEntity> iterable = classRoomRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

