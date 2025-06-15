package com.api_colllab.api_collab.persistence.dbdao;

import com.api_colllab.api_collab.persistence.entity.ForumEntity;

import java.util.List;
import java.util.Optional;


public interface IForumDAO {
    void createdForum(ForumEntity forumEntity);

    List<ForumEntity> allForumsUsers();

    Optional<ForumEntity> findById(Long idForum);
}
