package com.cinelist.api.tmdb;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TmdbMovieSpecification {
    private final Map<String, Object> filters = new HashMap<>();

    private TmdbMovieSpecification() {}

    public static TmdbMovieSpecification builder() {
        return new TmdbMovieSpecification();
    }

    public TmdbMovieSpecification withGenres(List<Integer> genreIds) {
        if (genreIds != null && !genreIds.isEmpty()) {
            filters.put("with_genres", genreIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));
        }
        return this;
    }

    public TmdbMovieSpecification withMaxRuntime(Integer maxRuntime) {
        if (maxRuntime != null) {
            filters.put("with_runtime.lte", maxRuntime);
        }
        return this;
    }

    public TmdbMovieSpecification withMinRating(Double minRating) {
        if (minRating != null) {
            filters.put("vote_average.gte", minRating);
        }
        return this;
    }

    public TmdbMovieSpecification withMinYear(Integer minYear) {
        if (minYear != null) {
            filters.put("primary_release_date.gte", minYear + "-01-01");
        }
        filters.put("primary_release_date.lte", LocalDate.now().toString());
        return this;
    }

    public Map<String, Object> build() {
        return filters;
    }
}
