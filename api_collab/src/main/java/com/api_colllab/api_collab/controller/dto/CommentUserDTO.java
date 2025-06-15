package com.api_colllab.api_collab.controller.dto;

import lombok.Builder;
@Builder
public record CommentUserDTO(Long id_forum,Long id_reaction) {
}
