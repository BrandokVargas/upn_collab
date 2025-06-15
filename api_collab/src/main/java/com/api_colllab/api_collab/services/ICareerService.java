package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.persistence.entity.CareerEntity;

import java.util.List;

public interface ICareerService {
    List<CareerEntity> findAll();
}
