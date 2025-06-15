package com.api_colllab.api_collab.persistence.repository;

import com.api_colllab.api_collab.persistence.entity.CareerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRepository extends CrudRepository<CareerEntity,Long> {

    List<CareerEntity> findCareerEntitiesByNameIn(List<String> careesNames);


}
