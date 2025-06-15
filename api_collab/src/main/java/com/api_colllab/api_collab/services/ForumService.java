package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.controller.dto.ForumsAllDTO;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;

import java.util.List;
import java.util.Optional;

public interface ForumService {

    void createdForum(ForumEntity forumEntity);

    List<ForumEntity> allForumsUsers();

    Optional<ForumEntity> findById(Long idForum);
}
