package com.api_colllab.api_collab.controller.dto;


import com.api_colllab.api_collab.persistence.entity.StateForumEntity;
import lombok.Builder;

@Builder
public record ForumUpdateStateRequest(Long id_state) {
}
