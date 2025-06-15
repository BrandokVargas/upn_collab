package com.api_colllab.api_collab.persistence.repository;

import com.api_colllab.api_collab.persistence.entity.NivelEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NivelRepository extends CrudRepository<NivelEntity,Long> {

    @Query("SELECT n FROM NivelEntity n WHERE n.name = :name")
    Optional<NivelEntity> findNameEntity(@Param("name") String name);

    NivelEntity findByName(String name);
}
