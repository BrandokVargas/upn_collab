package com.api_colllab.api_collab.controller.dto;

import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
public record CareersRequest(List<String> nameCareers) {
}
