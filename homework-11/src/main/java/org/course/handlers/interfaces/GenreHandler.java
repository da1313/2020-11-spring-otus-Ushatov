package org.course.handlers.interfaces;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface GenreHandler {

    Mono<ServerResponse> getGenres(ServerRequest request);

}
