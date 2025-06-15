package com.api_colllab.api_collab.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailRequest(@NotBlank String
                                   receiver,@NotBlank String subject, @NotBlank String  message) {
}
