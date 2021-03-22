package org.course.handlers.interfaces;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface BookHandler {

    Mono<ServerResponse> getBooks(ServerRequest request);

    Mono<ServerResponse> getBooksByGenre(ServerRequest request);

    Mono<ServerResponse> getBooksByQuery(ServerRequest request);

    Mono<ServerResponse> getBookById(ServerRequest request);

    Mono<ServerResponse> createBook(ServerRequest request);

    Mono<ServerResponse> updateBook(ServerRequest request);

    Mono<ServerResponse> deleteBook(ServerRequest request);

}
