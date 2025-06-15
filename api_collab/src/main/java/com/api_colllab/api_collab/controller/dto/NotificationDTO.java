package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

@Builder
public record NotificationDTO(String id_device,String title,String message) {
}
