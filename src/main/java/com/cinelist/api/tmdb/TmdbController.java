package com.cinelist.api.tmdb;

import com.cinelist.api.tmdb.dtos.MovieResponseDTO;
import com.cinelist.api.tmdb.dtos.MovieSummaryResponseDTO;
import com.cinelist.api.tmdb.dtos.TmdbPagedResponseDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/discover")
    public Mono<TmdbPagedResponseDTO<MovieSummaryResponseDTO>> discoverMovies(
            @RequestParam(required = false) List<Integer> genres,
            @RequestParam(required = false) Integer maxRuntime,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minYear
    ) {
        var spec = TmdbMovieSpecification.builder()
                .withGenres(genres)
                .withMaxRuntime(maxRuntime)
                .withMinRating(minRating)
                .withMinYear(minYear);

        return tmdbService.discoverMovies(spec);
    }

    @GetMapping("/trending/day")
    public Mono<TmdbPagedResponseDTO<MovieSummaryResponseDTO>> getTrendingToday() {
        return tmdbService.getTrendingMovies("day");
    }

    @GetMapping("/trending/week")
    public Mono<TmdbPagedResponseDTO<MovieSummaryResponseDTO>> getTrendingThisWeek() {
        return tmdbService.getTrendingMovies("week");
    }

    @GetMapping("/movie/{movieId}")
    public Mono<MovieResponseDTO> getMovie(@PathVariable("movieId") Long movieId) {
        return tmdbService.getMovieDetails(movieId);
    }

}
