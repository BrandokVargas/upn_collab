package com.api_colllab.api_collab.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String email_upn,
                               @NotBlank String password) {

}
