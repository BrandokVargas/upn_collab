package com.api_colllab.api_collab.persistence.dbdao.impl;

import com.api_colllab.api_collab.persistence.dbdao.IUserDAO;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAOImpl implements IUserDAO {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity createdUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> allUsers() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findById(Long idUser) {
        return userRepository.findById(idUser);
    }
}
