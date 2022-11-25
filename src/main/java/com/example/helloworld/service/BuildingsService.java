package com.example.helloworld.service;


import com.example.helloworld.entity.BuildingsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 业务层
 *
 * @author dialectics67
 * @since 2022-11-22 12:04:04
 */
public interface BuildingsService {

    void save(BuildingsEntity buildings);

    void deleteById(Integer id);

    List<BuildingsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer isValid, Integer isDel);

    Optional<BuildingsEntity> findById(Integer id);

    List<BuildingsEntity> findById(Collection<Integer> ids, Integer is_valid, Integer is_del);

}

