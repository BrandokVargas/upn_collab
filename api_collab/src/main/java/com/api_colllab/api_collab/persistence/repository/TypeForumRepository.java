package com.api_colllab.api_collab.persistence.repository;


import com.api_colllab.api_collab.persistence.entity.TypeForumEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeForumRepository extends CrudRepository<TypeForumEntity,Long> {
}
