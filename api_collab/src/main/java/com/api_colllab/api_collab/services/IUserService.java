package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.persistence.entity.UserEntity;

import java.util.List;

public interface IUserService {
    List<UserEntity> allUsers();
}
