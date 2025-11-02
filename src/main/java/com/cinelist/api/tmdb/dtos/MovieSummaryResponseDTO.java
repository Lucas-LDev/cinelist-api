package com.cinelist.api.tmdb.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MovieSummaryResponseDTO(
        Long id,
        String title,

        @JsonProperty("poster_path")
        String posterPath,

        @JsonProperty("release_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate releaseDate,

        @JsonProperty("vote_average")
        Double voteAverage,

        @JsonProperty("genre_ids")
        List<Integer> genreIds
) {}