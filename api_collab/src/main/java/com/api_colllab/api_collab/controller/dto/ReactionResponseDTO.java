package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

@Builder
public record ReactionResponseDTO(Long id_reaction,String comment) {
}
