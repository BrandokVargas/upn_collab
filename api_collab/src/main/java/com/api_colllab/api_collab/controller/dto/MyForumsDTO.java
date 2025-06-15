package com.api_colllab.api_collab.controller.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MyForumsDTO(String type_forum, String title, String name, LocalDate register,String name_state) {
}
