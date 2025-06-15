package com.api_colllab.api_collab.services.impl;

import com.api_colllab.api_collab.persistence.dbdao.IStateForumDAO;
import com.api_colllab.api_collab.persistence.entity.StateForumEntity;
import com.api_colllab.api_collab.services.IStateForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class StateForumServiceImpl implements IStateForumService {

    @Autowired
    private IStateForumDAO iStateForumDAO;
    @Override
    public Optional<StateForumEntity> findById(Long idState) {
        return iStateForumDAO.findById(idState);
    }
}
