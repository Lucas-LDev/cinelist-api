package com.cinelist.api.tmdb.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CastMemberResponseDTO(
    Integer id,
    String name,
    String character,
    @JsonProperty("profile_path")
    String profilePath
) {
}
