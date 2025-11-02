package com.cinelist.api.tmdb.dtos;

import java.util.List;

public record MovieListResponseDTO(
        List<MovieResponseDTO> movies,
        Integer totalResults
) {}
