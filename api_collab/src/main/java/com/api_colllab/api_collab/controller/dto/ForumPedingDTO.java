package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ForumPedingDTO(Long idForum,String type_forum, String title, String name, LocalDate register,
                             Boolean isFavorite,String url,Long id_user) {
}
