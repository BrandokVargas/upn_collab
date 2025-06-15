package com.api_colllab.api_collab.services.impl;

import com.api_colllab.api_collab.controller.dto.ForumsAllDTO;
import com.api_colllab.api_collab.persistence.dbdao.IForumDAO;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.services.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private IForumDAO iForumDAO;

    @Override
    public void createdForum(ForumEntity forumEntity) {
        iForumDAO.createdForum(forumEntity);
    }

    @Override
    public List<ForumEntity> allForumsUsers() {
        return iForumDAO.allForumsUsers();
    }

    @Override
    public Optional<ForumEntity> findById(Long idForum) {
        return iForumDAO.findById(idForum);
    }
}
