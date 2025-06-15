package com.api_colllab.api_collab.services.impl;


import com.api_colllab.api_collab.controller.dto.ForumsAllDTO;
import com.api_colllab.api_collab.controller.dto.MyForumsDTO;
import com.api_colllab.api_collab.controller.dto.ResponseMessageCustom;
import com.api_colllab.api_collab.persistence.dbdao.IForumDAO;
import com.api_colllab.api_collab.persistence.dbdao.IUserDAO;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;
import com.api_colllab.api_collab.services.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements IFavoriteService {

    @Autowired
    private IUserDAO iUserDAO;

    @Autowired
    private IForumDAO iForumDAO;

    @Override
    public ResponseMessageCustom addFavorite(Long usuarioId, Long foroId) {
        try{
            UserEntity user = iUserDAO.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            ForumEntity forums = iForumDAO.findById(foroId)
                    .orElseThrow(() -> new RuntimeException("Foro no encontrado"));

            user.getForumsFavorites().add(forums);
            iUserDAO.createdUser(user);
            return new ResponseMessageCustom("Foro agregado a la lista de favoritos");
        }catch (RuntimeException e){
            return new ResponseMessageCustom(e.getMessage());
        }

    }

    @Override
    public ResponseMessageCustom removeFavorite(Long usuarioId, Long foroId) {
        try{
            UserEntity user = iUserDAO.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            ForumEntity forums = iForumDAO.findById(foroId)
                    .orElseThrow(() -> new RuntimeException("Foro no encontrado"));

            user.getForumsFavorites().remove(forums);
            iUserDAO.createdUser(user);
            return new ResponseMessageCustom("Foro eliminado de tu lista de favoritos");
        }catch (RuntimeException e){
            return new ResponseMessageCustom(e.getMessage());
        }

    }

    @Override
    public List<ForumEntity> getForumFavorite(Long usuarioId) {
        UserEntity user = iUserDAO.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getForumsFavorites();
    }
}
