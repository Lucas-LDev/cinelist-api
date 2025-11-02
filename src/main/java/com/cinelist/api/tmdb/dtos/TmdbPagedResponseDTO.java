package com.cinelist.api.tmdb.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbPagedResponseDTO<T>(
        Integer page,
        List<T> results,

        @JsonProperty("total_pages")
        Integer totalPages,

        @JsonProperty("total_results")
        Integer totalResults
) {}