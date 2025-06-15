package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

@Builder
public record ProfileUserDto(String name,String email_upn,String nivel_name) {
}
