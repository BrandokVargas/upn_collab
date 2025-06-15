package com.api_colllab.api_collab.persistence.dbdao;

import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.persistence.entity.StateForumEntity;

import java.util.Optional;

public interface IStateForumDAO {

    Optional<StateForumEntity> findById(Long idState);
}
