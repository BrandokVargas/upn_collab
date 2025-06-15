package com.api_colllab.api_collab.services.impl;

import com.api_colllab.api_collab.controller.dto.ResponseMessageCustom;
import com.api_colllab.api_collab.persistence.dbdao.IForumDAO;
import com.api_colllab.api_collab.persistence.dbdao.IReactionDAO;
import com.api_colllab.api_collab.persistence.dbdao.IUserDAO;
import com.api_colllab.api_collab.persistence.entity.CommentUserForosDetails;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.persistence.entity.ReactionEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private IForumDAO iForumDAO;

    @Autowired
    private IReactionDAO iReactionDAO;

    @Override
    public ResponseMessageCustom addCommentUserForum(Long id_forum, Long id_reaction, Long id_user) {
        try{
            UserEntity user = iUserDAO.findById(id_user)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            ForumEntity forums = iForumDAO.findById(id_forum)
                    .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
            ReactionEntity reaction = iReactionDAO.findById(id_reaction)
                    .orElseThrow(() -> new RuntimeException("Reaction no encontrada"));

            CommentUserForosDetails coments = CommentUserForosDetails.builder()
                    .user_id(user)
                    .dateComment(LocalDate.now())
                    .forum_id(forums)
                    .reaction_id(reaction)
                    .build();
            forums.getComments().add(coments);
            iForumDAO.createdForum(forums);

            return new ResponseMessageCustom("Comentario enviado");
        }catch (RuntimeException e){
            return new ResponseMessageCustom(e.getMessage());
        }
    }


    @Override
    public List<CommentUserForosDetails> getAllComments(Long id_forum) {
        ForumEntity forum = iForumDAO.findById(id_forum)
                .orElseThrow(() -> new RuntimeException("Foro no encontrado"));
        return forum.getComments();
    }
}
