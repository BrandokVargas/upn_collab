package com.api_colllab.api_collab.persistence.repository;


import com.api_colllab.api_collab.persistence.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity,Long> {

    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleName);

}
