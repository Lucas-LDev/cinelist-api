package com.cinelist.api.tmdb.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreditsResponseDTO(
        List<CastMemberResponseDTO> cast
) {
}
