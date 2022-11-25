package com.example.helloworld.repository;

import com.example.helloworld.entity.BedsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedsRepository extends CrudRepository<BedsEntity, Integer> {
    List<BedsEntity> findAllByIsValidAndIsDelOrderByOrderNum(Integer is_valid, Integer is_del);

    List<BedsEntity> findAllByIdInAndIsValidAndIsDel(Iterable<Integer> ids, Integer is_valid, Integer is_del);

    List<BedsEntity> findAllByRoomIdInAndIsValidAndIsDel(Iterable<Integer> roomIds, Integer is_valid, Integer is_del);
}
