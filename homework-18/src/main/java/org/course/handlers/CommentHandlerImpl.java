package org.course.handlers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import org.course.api.pojo.BookCount;
import org.course.api.pojo.CommentShort;
import org.course.api.requests.CommentListRequest;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.api.responces.ErrorResponse;
import org.course.api.responces.ResultResponse;
import org.course.domain.Book;
import org.course.handlers.interfaces.CommentHandler;
import org.course.domain.Comment;
import org.course.repositories.BookRepository;
import org.course.repositories.CommentRepository;
import org.course.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentHandlerImpl implements CommentHandler {

    private static final String SERVICE_CONFIG_NAME = "defaultHandler";

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @CircuitBreaker(name = SERVICE_CONFIG_NAME, fallbackMethod = "commentFallbackHandler")
    @TimeLimiter(name = SERVICE_CONFIG_NAME, fallbackMethod = "commentFallbackHandler")
    @Override
    public Mono<ServerResponse> getComments(ServerRequest request){
        return Mono.just(request).flatMap(this::getRequest).flatMap(r -> Mono.zip(Mono.just(r), bookRepository.findById(r.getId())))
                .flatMap(z -> {
                    Mono<Integer> totalPages = commentRepository.findCountByBook(z.getT2().getId())
                            .defaultIfEmpty(new BookCount(0)).map(c -> getTotalPages(c.getValue(), z.getT1().getPageSize()));
                    Mono<List<CommentShort>> comments = commentRepository.findByBookId(z.getT2().getId(), getPageRequest(z.getT1()))
                            .map(c -> new CommentShort(c.getId(), c.getText(), c.getUser().getName())).collectList();
                    return Mono.zip(comments, totalPages);
                }).map(z -> new CommentListResponse(z.getT1(), z.getT2()))
                .flatMap(commentListResponse -> ServerResponse.ok().bodyValue(commentListResponse))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ResultResponse(false)))
                .onErrorResume(e -> ServerResponse.ok().bodyValue(new ErrorResponse(e.getMessage())));
    }

    @CircuitBreaker(name = SERVICE_CONFIG_NAME, fallbackMethod = "commentFallbackHandler")
    @TimeLimiter(name = SERVICE_CONFIG_NAME, fallbackMethod = "commentFallbackHandler")
    @Override
    public Mono<ServerResponse> createComment(ServerRequest request){
        return request.bodyToMono(CommentRequest.class)
                .flatMap(r -> Mono.zip(
                        Mono.just(r),
                        userRepository.findAll().next(),
                        bookRepository.findById(r.getId())
                ))
                .flatMap(z -> commentRepository.save(Comment.of(z.getT1().getText(), z.getT2(), z.getT3())))
                .flatMap(c -> bookRepository.increaseCommentCount(c.getBook().getId()))
                .map(r -> r.getModifiedCount() > 0 ? r : null)
                .hasElement().map(ResultResponse::new)
                .flatMap(r -> ServerResponse.ok().bodyValue(r));
    }

    private PageRequest getPageRequest(CommentListRequest request){
        return PageRequest.of(request.getPageNumber(), request.getPageSize());
    }

    private int getTotalPages(long count, int pageSize){
        return pageSize == 0 ? 0 : (int) Math.ceil(((double) count) / pageSize);
    }

    private Mono<CommentListRequest> getRequest(ServerRequest request){
        String id = request.queryParam("id").orElseThrow(() -> new NoSuchElementException("Missing query parameter id"));
        int pageSize = Integer.parseInt(request.queryParam("pageSize").orElseThrow(() -> new NoSuchElementException("Missing query parameter pageSize")));
        int pageNumber = Integer.parseInt(request.queryParam("pageNumber").orElseThrow(() -> new NoSuchElementException("Missing query parameter pageNumber")));
        return Mono.just(new CommentListRequest(id, pageSize, pageNumber));
    }

    private Mono<ServerResponse> commentFallbackHandler(ServerRequest request, Throwable throwable){
        return ServerResponse.ok().bodyValue(Collections.singletonMap("result", "false"));
    }

}
