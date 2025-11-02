package com.cinelist.api.tmdb.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MovieResponseDTO(
    Long id,
    String title,
    String overview,

    @JsonProperty(value = "poster_path", access = JsonProperty.Access.WRITE_ONLY)
    String posterPath,

    @JsonProperty(value = "backdrop_path", access = JsonProperty.Access.WRITE_ONLY)
    String backdropPath,

    @JsonProperty(value = "release_date", access = JsonProperty.Access.WRITE_ONLY)
    LocalDate releaseDate,

    @JsonProperty("vote_average")
    Double voteAverage,

    Integer runtime,
    List<GenreResponseDTO> genres,
    CreditsResponseDTO credits
) {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    @JsonProperty("poster_url")
    public String getPosterUrl() {
        return posterPath != null ? IMAGE_BASE_URL + "w300" + posterPath : null;
    }

    @JsonProperty("backdrop_url")
    public String getBackdropUrl() {
        return backdropPath != null ? IMAGE_BASE_URL + "w1920" + backdropPath : null;
    }

    @JsonProperty("release_date")
    public String formattedDate() {
        return releaseDate != null ? releaseDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
    }
}
