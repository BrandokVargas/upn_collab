package com.api_colllab.api_collab.persistence.dbdao;

import com.api_colllab.api_collab.persistence.entity.ReactionEntity;
import com.api_colllab.api_collab.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IReactionDAO {


    List<ReactionEntity> getAllReactionsComments();

    Optional<ReactionEntity> findById(Long idReaction);

}
