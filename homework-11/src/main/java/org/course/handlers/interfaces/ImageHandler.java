package org.course.handlers.interfaces;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ImageHandler {

    Mono<ServerResponse> getImage(ServerRequest request);

}
