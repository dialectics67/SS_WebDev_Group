package com.example.helloworld.repository;

import com.example.helloworld.entity.BuildingsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BuildingsRepository extends CrudRepository<BuildingsEntity, Integer> {

    List<BuildingsEntity> findByIdInAndIsValidAndIsDelOrderByOrderNum(Collection<Integer> id, Integer isValid, Integer isDel);

    List<BuildingsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer isValid, Integer isDel);

}
