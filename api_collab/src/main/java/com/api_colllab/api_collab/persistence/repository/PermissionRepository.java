package com.api_colllab.api_collab.persistence.repository;

import com.api_colllab.api_collab.persistence.entity.PermissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<PermissionEntity,Long> {
}
