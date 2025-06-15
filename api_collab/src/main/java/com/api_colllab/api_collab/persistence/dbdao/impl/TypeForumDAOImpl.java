package com.api_colllab.api_collab.persistence.dbdao.impl;

import com.api_colllab.api_collab.persistence.dbdao.ITypeForumDAO;
import com.api_colllab.api_collab.persistence.entity.TypeForumEntity;
import com.api_colllab.api_collab.persistence.repository.TypeForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeForumDAOImpl implements ITypeForumDAO {

    @Autowired
    private TypeForumRepository typeForumRepository;
    @Override
    public List<TypeForumEntity> getTypeForums() {
        return (List<TypeForumEntity>)typeForumRepository.findAll();
    }
}
