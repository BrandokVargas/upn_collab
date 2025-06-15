package com.api_colllab.api_collab.persistence.dbdao.impl;

import com.api_colllab.api_collab.persistence.dbdao.IStateForumDAO;
import com.api_colllab.api_collab.persistence.entity.StateForumEntity;
import com.api_colllab.api_collab.persistence.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class StateForumDAOImpl implements IStateForumDAO {
    @Autowired
    private StateRepository stateRepository;
    @Override
    public Optional<StateForumEntity> findById(Long idState) {
        return stateRepository.findById(idState);
    }
}
