package com.example.helloworld.repository;

import com.example.helloworld.entity.BedsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BedsRepository extends CrudRepository<BedsEntity, Integer> {

    List<BedsEntity> findAllByStatusAndIsValidAndIsDelOrderByOrderNum(Integer status, Integer isValid, Integer isDel);

    List<BedsEntity> findAllByRoomIdAndStatusAndIsValidAndIsDelOrderByOrderNum(Integer roomId, Integer status, Integer isValid, Integer isDel);

    Optional<BedsEntity> findByUidAndStatusAndIsValidAndIsDel(Integer uid, Integer status, Integer is_valid, Integer is_del);

}
