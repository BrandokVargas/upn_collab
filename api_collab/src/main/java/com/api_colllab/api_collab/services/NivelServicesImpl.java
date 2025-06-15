package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.persistence.entity.NivelEntity;
import com.api_colllab.api_collab.persistence.repository.NivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NivelServicesImpl implements  NivelServices{

    @Autowired
    private NivelRepository nivelRepository;

    @Override
    public NivelEntity getNivelEntity(String nivelName) {
        return nivelRepository.findNameEntity(nivelName).orElseThrow(() -> new RuntimeException("Nivel not found"));
    }
}
