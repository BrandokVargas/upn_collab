package com.api_colllab.api_collab.persistence.repository;

import com.api_colllab.api_collab.persistence.entity.ReactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends CrudRepository<ReactionEntity,Long> {
}
