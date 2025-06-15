package com.api_colllab.api_collab.persistence.dbdao;

import com.api_colllab.api_collab.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {


    UserEntity createdUser(UserEntity user);
    List<UserEntity> allUsers();

    Optional<UserEntity> findById(Long idUser);

}
