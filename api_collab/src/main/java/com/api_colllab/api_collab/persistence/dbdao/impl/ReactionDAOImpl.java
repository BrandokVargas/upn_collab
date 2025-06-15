package com.api_colllab.api_collab.persistence.dbdao.impl;

import com.api_colllab.api_collab.persistence.dbdao.IReactionDAO;
import com.api_colllab.api_collab.persistence.entity.ReactionEntity;
import com.api_colllab.api_collab.persistence.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class ReactionDAOImpl implements IReactionDAO {


    @Autowired
    private ReactionRepository reactionRepository;

    @Override
    public List<ReactionEntity> getAllReactionsComments() {
        return (List<ReactionEntity>) reactionRepository.findAll();
    }

    @Override
    public Optional<ReactionEntity> findById(Long idReaction) {
        return reactionRepository.findById(idReaction);
    }
}
