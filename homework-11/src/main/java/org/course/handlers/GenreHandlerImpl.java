package org.course.handlers;

import lombok.AllArgsConstructor;
import org.course.handlers.interfaces.GenreHandler;
import org.course.repositories.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GenreHandlerImpl implements GenreHandler {

    private final GenreRepository genreRepository;

    @Override
    public Mono<ServerResponse> getGenres(ServerRequest request){
        return genreRepository.findAll().collectList().flatMap(g -> ServerResponse.ok().bodyValue(g));
    }

}
