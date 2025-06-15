package com.api_colllab.api_collab.persistence.repository;

import com.api_colllab.api_collab.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email" )
    Optional<UserEntity> findEmail(@Param("email") String email);


    @Query("SELECT u FROM UserEntity u WHERE u.email = :email" )
    UserEntity findUser(@Param("email") String email);




}
