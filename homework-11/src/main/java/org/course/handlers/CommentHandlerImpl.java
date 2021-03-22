package org.course.handlers;

import lombok.AllArgsConstructor;
import org.course.api.pojo.BookCount;
import org.course.api.pojo.CommentShort;
import org.course.api.requests.CommentListRequest;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.api.responces.ErrorResponse;
import org.course.api.responces.ResultResponse;
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

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentHandlerImpl implements CommentHandler {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Override
    public Mono<ServerResponse> getComments(ServerRequest request){
        return getRequest(request).flatMap(r -> Mono.zip(Mono.just(r), bookRepository.findById(r.getId())))
                .flatMap(z1 -> Mono.zip(
                        Mono.just(z1.getT1().getPageSize()),
                        commentRepository.findByBookId(z1.getT2().getId(), getPageRequest(z1.getT1())).collectList(),
                        commentRepository.findCountByBook(z1.getT2().getId()).defaultIfEmpty(new BookCount(0))))
                .map(z2 -> Tuples.of(z2.getT2(), getTotalPages(z2.getT3().getValue(), z2.getT1())))
                .map(z3 -> new CommentListResponse(
                        z3.getT1().stream().map(c -> new CommentShort(c.getId(), c.getText(), c.getUser().getName()))
                                .collect(Collectors.toList()),
                        z3.getT2()))
                .flatMap(commentListResponse -> ServerResponse.ok().bodyValue(commentListResponse))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ResultResponse(false)))
                .onErrorResume(e -> ServerResponse.ok().bodyValue(new ErrorResponse(e.getMessage())));
    }

    @Override
    public Mono<ServerResponse> createComment(ServerRequest request){
        return request.bodyToMono(CommentRequest.class)
                .flatMap(r -> Mono.zip(
                        Mono.just(r),
                        userRepository.findAll().collectList(),
                        bookRepository.findById(r.getId())
                ))
                .map(z1 -> Tuples.of(z1.getT1(), z1.getT2().get(0), z1.getT3()))
                .flatMap(t -> commentRepository.save(Comment.of(t.getT1().getText(), t.getT2(), t.getT3())))
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
        return Mono.just(request).flatMap(r -> Mono.zip(
                Mono.just(request.queryParam("id").orElseThrow(() -> new NoSuchElementException("Missing query parameter id"))),
                Mono.just(Integer.parseInt(request.queryParam("pageSize").orElseThrow(() -> new NoSuchElementException("Missing query parameter pageSize")))),
                Mono.just(Integer.parseInt(request.queryParam("pageNumber").orElseThrow(() -> new NoSuchElementException("Missing query parameter pageNumber"))))))
                .map(z -> new CommentListRequest(z.getT1(), z.getT2(), z.getT3()));
    }

}
