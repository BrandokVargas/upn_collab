package com.api_colllab.api_collab.persistence.repository;

import com.api_colllab.api_collab.persistence.entity.StateForumEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StateRepository extends CrudRepository<StateForumEntity,Long> {
    @Query("SELECT s FROM StateForumEntity s WHERE s.nameState = :nameState" )
    Optional<StateForumEntity> findByName(@Param("nameState") String nameState);
}
