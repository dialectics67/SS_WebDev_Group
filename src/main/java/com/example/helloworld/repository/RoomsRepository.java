package com.example.helloworld.repository;

import com.example.helloworld.entity.RoomsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomsRepository extends CrudRepository<RoomsEntity, Integer> {
    List<RoomsEntity> findAllByGenderAndIsValidAndIsDelOrderByOrderNum(Integer gender, Integer is_valid, Integer is_del);

    List<RoomsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer is_valid, Integer is_del);

    List<RoomsEntity> findAllByBuildingIdAndGenderAndIsValidAndIsDelOrderByOrderNum(Integer buildingId, Integer gender, Integer isValid, Integer isDel);
}
