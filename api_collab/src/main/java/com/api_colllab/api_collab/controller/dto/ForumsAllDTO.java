package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ForumsAllDTO(Long idForum, String type_forum, String title, String name, LocalDate register, String hour,
                           Boolean isFavorite) {
}
