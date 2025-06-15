package com.api_colllab.api_collab.services.impl;

import com.api_colllab.api_collab.persistence.dbdao.IReactionDAO;
import com.api_colllab.api_collab.persistence.entity.ReactionEntity;
import com.api_colllab.api_collab.services.IReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReactionServiceImpl implements IReactionService {

    @Autowired
    private IReactionDAO iReactionDAO;
    @Override
    public List<ReactionEntity> getAllReactionsComments() {
        return iReactionDAO.getAllReactionsComments();
    }
}
