    package com.cinelist.api.tmdb.dtos;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GenreResponseDTO(
        Integer id,
        String name
    ) {
    }
