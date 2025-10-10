package com.cinelist.api.infra.exceptions;

public record ErrorResponseDTO(
        int status,
        String msg,
        long timestamp
) {
}
