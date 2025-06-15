package com.api_colllab.api_collab.controller.dto;

import lombok.Builder;

@Builder
public record ResponseNotification(int rpta, String message, NotificationDTO notification) {
}
