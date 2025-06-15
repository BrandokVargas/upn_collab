package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ForumEntryDTO(Long id_forum ,String title, String name_user, String content, String url, LocalDate date_register) {
}
