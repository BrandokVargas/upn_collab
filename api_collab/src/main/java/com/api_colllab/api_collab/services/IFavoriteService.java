package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.controller.dto.ResponseMessageCustom;
import com.api_colllab.api_collab.persistence.entity.ForumEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;

import java.util.List;

public interface IFavoriteService {

    ResponseMessageCustom addFavorite(Long usuarioId, Long foroId);
    ResponseMessageCustom removeFavorite(Long usuarioId, Long foroId);
    List<ForumEntity> getForumFavorite(Long usuarioId);

}
