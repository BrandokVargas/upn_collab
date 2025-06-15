package com.api_colllab.api_collab.services;

import com.api_colllab.api_collab.persistence.entity.ReactionEntity;

import java.util.List;

public interface IReactionService {
    List<ReactionEntity> getAllReactionsComments();
}
