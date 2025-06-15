package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.persistence.entity.StateForumEntity;

import java.util.Optional;

public interface IStateForumService {

    Optional<StateForumEntity> findById(Long idState);
}
