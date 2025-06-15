package com.api_colllab.api_collab.services.impl;

import com.api_colllab.api_collab.persistence.dbdao.IUserDAO;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDAO iUserDAO;
    @Override
    public List<UserEntity> allUsers() {
        return iUserDAO.allUsers();
    }
}
