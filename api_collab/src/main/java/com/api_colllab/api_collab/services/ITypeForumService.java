package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.persistence.entity.TypeForumEntity;

import java.util.List;

public interface ITypeForumService {
    List<TypeForumEntity> getTypeForums();
}
