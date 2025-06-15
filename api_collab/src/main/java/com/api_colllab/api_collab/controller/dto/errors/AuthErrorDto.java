package com.api_colllab.api_collab.controller.dto.errors;

import lombok.Builder;
import lombok.Data;


@Builder
public record AuthErrorDto (String code,String message) {
}
