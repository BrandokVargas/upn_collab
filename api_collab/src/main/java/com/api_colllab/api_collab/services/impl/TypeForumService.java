package com.api_colllab.api_collab.services.impl;

import com.api_colllab.api_collab.persistence.dbdao.ITypeForumDAO;
import com.api_colllab.api_collab.persistence.entity.TypeForumEntity;
import com.api_colllab.api_collab.services.ITypeForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TypeForumService implements ITypeForumService {
    @Autowired
    private ITypeForumDAO iTypeForumDAO;
    @Override
    public List<TypeForumEntity> getTypeForums() {
        return iTypeForumDAO.getTypeForums();
    }
}
