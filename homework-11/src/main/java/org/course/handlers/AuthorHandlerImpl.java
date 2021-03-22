package org.course.handlers;

import lombok.AllArgsConstructor;
import org.course.handlers.interfaces.AuthorHandler;
import org.course.repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthorHandlerImpl implements AuthorHandler {

    private final AuthorRepository authorRepository;

    @Override
    public Mono<ServerResponse> getAuthors(ServerRequest request){
        return authorRepository.findAll().collectList().flatMap(a -> ServerResponse.ok().bodyValue(a));
    }

}
