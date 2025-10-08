package com.cinelist.api.user.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {}