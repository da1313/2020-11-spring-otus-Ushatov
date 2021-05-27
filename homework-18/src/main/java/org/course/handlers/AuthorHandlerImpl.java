package org.course.handlers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import org.course.handlers.interfaces.AuthorHandler;
import org.course.repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthorHandlerImpl implements AuthorHandler {

    private static final String SERVICE_CONFIG_NAME = "defaultHandler";

    private final AuthorRepository authorRepository;

    @CircuitBreaker(name = SERVICE_CONFIG_NAME, fallbackMethod = "authorFallbackHandler")
    @TimeLimiter(name = SERVICE_CONFIG_NAME, fallbackMethod = "authorFallbackHandler")
    @Override
    public Mono<ServerResponse> getAuthors(ServerRequest request){
        return authorRepository.findAll().collectList().flatMap(a -> ServerResponse.ok().bodyValue(a));
    }

    private Mono<ServerResponse> authorFallbackHandler(ServerRequest request, Throwable throwable){
        return ServerResponse.ok().bodyValue(Collections.singletonMap("result", "false"));
    }

}
