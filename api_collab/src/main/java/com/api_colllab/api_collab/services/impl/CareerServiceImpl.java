package com.api_colllab.api_collab.services.impl;


import com.api_colllab.api_collab.persistence.dbdao.ICareerDAO;
import com.api_colllab.api_collab.persistence.entity.CareerEntity;
import com.api_colllab.api_collab.services.ICareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerServiceImpl implements ICareerService {

    @Autowired
    private ICareerDAO iCareerDAO;

    @Override
    public List<CareerEntity> findAll() {
        return iCareerDAO.findAll();
    }
}
