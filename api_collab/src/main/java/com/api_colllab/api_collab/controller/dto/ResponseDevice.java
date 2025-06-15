package com.api_colllab.api_collab.controller.dto;


import lombok.Builder;

@Builder
public record ResponseDevice(Long id, String token_device, int rpta, RequestDeviceDTO body) {
}

