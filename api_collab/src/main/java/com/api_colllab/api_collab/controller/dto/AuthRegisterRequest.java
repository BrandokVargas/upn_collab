package com.api_colllab.api_collab.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthRegisterRequest(@NotBlank String cod_upn, @NotBlank String name,
                                  @NotBlank String lastname, @NotBlank String email,
                                  @Valid CareersRequest careersRequest) {
}
