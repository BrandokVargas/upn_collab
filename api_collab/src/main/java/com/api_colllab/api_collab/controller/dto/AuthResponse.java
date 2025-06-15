package com.api_colllab.api_collab.controller.dto;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email_upn","message","jwt","status"})
public record AuthResponse(String email_upn,
                           String message,
                           String jwt,
                           String jwt_refresh,
                           boolean status) {
}
