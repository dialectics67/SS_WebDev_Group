package com.example.helloworld.service.impl;


import com.example.helloworld.entity.StudentInfoEntity;
import com.example.helloworld.repository.StudentInfoRepository;
import com.example.helloworld.service.StudentInfoService;
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
public class StudentInfoServiceImpl implements StudentInfoService {

    @Resource
    private StudentInfoRepository studentInfoRepository;

    @Override
    public void save(StudentInfoEntity studentInfo) {
        studentInfoRepository.save(studentInfo);
    }

    @Override
    public void deleteById(Integer id) {
        studentInfoRepository.deleteById(id);
    }

    @Override
    public Optional<StudentInfoEntity> findById(Integer id) {
        return studentInfoRepository.findById(id);
    }

    @Override
    public Optional<StudentInfoEntity> findByUid(Integer uid) {
        return studentInfoRepository.findByUid(uid);
    }

    @Override
    public List<StudentInfoEntity> findById(Collection<Integer> ids) {
        Iterable<StudentInfoEntity> iterable = studentInfoRepository.findAllById(ids);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

}

