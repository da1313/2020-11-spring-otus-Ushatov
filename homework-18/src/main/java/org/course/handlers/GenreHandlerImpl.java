package org.course.handlers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import org.course.handlers.interfaces.GenreHandler;
import org.course.repositories.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@AllArgsConstructor
public class GenreHandlerImpl implements GenreHandler {

    private static final String SERVICE_CONFIG_NAME = "defaultHandler";

    private final GenreRepository genreRepository;

    @CircuitBreaker(name = SERVICE_CONFIG_NAME, fallbackMethod = "genreFallbackHandler")
    @TimeLimiter(name = SERVICE_CONFIG_NAME, fallbackMethod = "genreFallbackHandler")
    @Override
    public Mono<ServerResponse> getGenres(ServerRequest request){
        return genreRepository.findAll().collectList().flatMap(g -> ServerResponse.ok().bodyValue(g));
    }

    private Mono<ServerResponse> genreFallbackHandler(ServerRequest request, Throwable throwable){
        return ServerResponse.ok().bodyValue(Collections.singletonMap("result", "false"));
    }

}
