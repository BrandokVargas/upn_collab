package com.api_colllab.api_collab.persistence.dbdao.impl;

import com.api_colllab.api_collab.persistence.dbdao.ICareerDAO;
import com.api_colllab.api_collab.persistence.entity.CareerEntity;
import com.api_colllab.api_collab.persistence.repository.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CareerDAOImpl implements ICareerDAO {

    @Autowired
    private CareerRepository careerRepository;

    @Override
    public List<CareerEntity> findAll() {
        return (List<CareerEntity>) careerRepository.findAll();
    }
}
