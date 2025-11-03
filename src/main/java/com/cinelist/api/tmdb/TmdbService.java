package com.cinelist.api.tmdb;

import com.cinelist.api.tmdb.dtos.MovieResponseDTO;
import com.cinelist.api.tmdb.dtos.MovieSummaryResponseDTO;
import com.cinelist.api.tmdb.dtos.TmdbPagedResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class TmdbService {
    private final WebClient tmdbWebClient;

    public TmdbService(WebClient tmdbWebClient) {
        this.tmdbWebClient = tmdbWebClient;
    }

    public Mono<TmdbPagedResponseDTO<MovieSummaryResponseDTO>> getTrendingMovies(String timeWindow) {
        return tmdbWebClient.get()
                .uri("/trending/movie/{time_window}?language=pt-BR", timeWindow)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TmdbPagedResponseDTO<MovieSummaryResponseDTO>>() {});
    }

    private Mono<List<MovieSummaryResponseDTO>> fetchMoviesPage(TmdbMovieSpecification spec, int page) {
        return tmdbWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/discover/movie")
                            .queryParam("language", "pt-BR")
                            .queryParam("release_date.lte", LocalDate.now())
                            .queryParam("with_runtime.gte", 80)
                            .queryParam("vote_count.gte", 2000)
                            .queryParam("include_adult", false)
                            .queryParam("sort_by", "vote_average.desc")
                            .queryParam("page", page);
                    spec.build().forEach(builder::queryParam);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TmdbPagedResponseDTO<MovieSummaryResponseDTO>>() {})
                .map(TmdbPagedResponseDTO::results);
    }

    public Mono<TmdbPagedResponseDTO<MovieSummaryResponseDTO>> discoverMovies(TmdbMovieSpecification spec) {
        return Flux.range(1, 3)
                .flatMap(page -> fetchMoviesPage(spec, page))
                .flatMap(Flux::fromIterable)
                .sort(Comparator.comparingDouble(MovieSummaryResponseDTO::voteAverage).reversed())
                .take(50)
                .collectList()
                .map(list -> new TmdbPagedResponseDTO<>(1, list, 1, list.size()));
    }



    public Mono<MovieResponseDTO> getMovieDetails(Long movieId) {
        return tmdbWebClient.get()
                .uri("/movie/{movieId}?language=pt-BR&append_to_response=credits", movieId)
                .retrieve()
                .bodyToMono(MovieResponseDTO.class);
    }
}
