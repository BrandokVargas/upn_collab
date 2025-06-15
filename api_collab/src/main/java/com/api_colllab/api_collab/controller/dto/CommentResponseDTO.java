package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CommentResponseDTO(Long idComment, String name, String comment, LocalDate registerComment) {
}
