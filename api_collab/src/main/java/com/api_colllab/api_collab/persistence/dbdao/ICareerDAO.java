package com.api_colllab.api_collab.persistence.dbdao;

import com.api_colllab.api_collab.persistence.entity.CareerEntity;

import java.util.List;

public interface ICareerDAO {

    //Obtener la lista de carreras
    List<CareerEntity> findAll();

}
