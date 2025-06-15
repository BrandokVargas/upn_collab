package com.api_colllab.api_collab.persistence.dbdao.impl;

import com.api_colllab.api_collab.controller.dto.ForumsAllDTO;
import com.api_colllab.api_collab.persistence.dbdao.IForumDAO;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.persistence.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class IForumDAOImpl implements IForumDAO {
    @Autowired
    private ForumRepository forumRepository;
    @Override
    public void createdForum(ForumEntity forumEntity) {
        forumRepository.save(forumEntity);
    }

    @Override
    public List<ForumEntity> allForumsUsers() {
        return (List<ForumEntity>) forumRepository.findAll();
    }

    @Override
    public Optional<ForumEntity> findById(Long idForum) {
        return forumRepository.findById(idForum);
    }


}
