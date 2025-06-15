package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.controller.dto.ResponseMessageCustom;
import com.api_colllab.api_collab.persistence.entity.CommentUserForosDetails;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;

import java.util.List;

public interface ICommentService {

    ResponseMessageCustom addCommentUserForum(Long id_forum,Long id_reaction,Long id_user);

    List<CommentUserForosDetails> getAllComments(Long id_forum);

}
