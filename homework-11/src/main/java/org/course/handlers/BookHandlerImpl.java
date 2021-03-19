package org.course.handlers;

import lombok.AllArgsConstructor;
import org.course.api.pojo.BookCount;
import org.course.api.requests.BookListRequest;
import org.course.api.requests.BookRequest;
import org.course.api.responces.BookInfoResponse;
import org.course.api.responces.BookListResponse;
import org.course.api.responces.ErrorResponse;
import org.course.api.responces.ResultResponse;
import org.course.handlers.interfaces.BookHandler;
import org.course.domain.Book;
import org.course.domain.embedded.BookSort;
import org.course.repositories.AuthorRepository;
import org.course.repositories.BookRepository;
import org.course.repositories.GenreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;


@Service
@AllArgsConstructor
public class BookHandlerImpl implements BookHandler {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Mono<ServerResponse> getBooks(ServerRequest request){
        return getRequest(request).flatMap(r -> Mono.zip(
                Mono.just(r.getPageSize()),
                    bookRepository.findAllBookShortAuto(getPageRequest(r)).collectList(),
                    bookRepository.count()))
                .map(z -> new BookListResponse(z.getT2(), getTotalPages(z.getT3(), z.getT1())))
                .flatMap(r -> ServerResponse.ok().bodyValue(r))
                .onErrorResume(e -> ServerResponse.ok().bodyValue(new ErrorResponse(e.getMessage())));
    }

    @Override
    public Mono<ServerResponse> getBooksByGenre(ServerRequest request){
        return Mono.just(request).flatMap(r -> Mono.zip(getRequest(r), getGenreId(r)))
                .flatMap(r -> Mono.zip(
                        Mono.just(r.getT1().getPageSize()),
                        bookRepository.findAllBookShortByGenre(r.getT2(), getPageRequest(r.getT1())).collectList(),
                        bookRepository.findCountByGenres(r.getT2()).defaultIfEmpty(new BookCount(0))))
                .map(z -> new BookListResponse(z.getT2(), getTotalPages(z.getT3().getValue(), z.getT1())))
                .flatMap(r -> ServerResponse.ok().bodyValue(r))
                .onErrorResume(e -> ServerResponse.ok().bodyValue(new ErrorResponse(e.getMessage())));
    }

    @Override
    public Mono<ServerResponse> getBooksByQuery(ServerRequest request){
        return Mono.just(request).flatMap(r -> Mono.zip(getRequest(r), getQuery(r)))
                .flatMap(r -> Mono.zip(
                        Mono.just(r.getT1().getPageSize()),
                        bookRepository.findAllBookShortByQuery(r.getT2(), getPageRequest(r.getT1())).collectList(),
                        bookRepository.findCountByQuery(r.getT2()).defaultIfEmpty(new BookCount(0))))
                .map(z -> new BookListResponse(z.getT2(), getTotalPages(z.getT3().getValue(), z.getT1())))
                .flatMap(r -> ServerResponse.ok().bodyValue(r))
                .onErrorResume(e -> ServerResponse.ok().bodyValue(new ErrorResponse(e.getMessage())));
    }

    @Override
    public Mono<ServerResponse> getBookById(ServerRequest request){
        return bookRepository.findById(request.pathVariable("id"))
                .map(BookInfoResponse::ofBook)
                .flatMap(b -> ServerResponse.ok().bodyValue(b))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(new ResultResponse(false)));
    }

    @Override
    public Mono<ServerResponse> createBook(ServerRequest request){
        return request.bodyToMono(BookRequest.class)
                .flatMap(r -> Mono.zip(Mono.just(r), authorRepository.findById(r.getAuthorId()),
                        genreRepository.findByIdIn(r.getGenresId()).collectList()))
                .flatMap(t -> bookRepository.save(Book.of(t.getT1().getTitle(), t.getT2(), t.getT3()))
                .hasElement()
                .map(ResultResponse::new))
                .flatMap(r -> ServerResponse.ok().bodyValue(r));
    }

    @Override
    public Mono<ServerResponse> updateBook(ServerRequest request){
        return request.bodyToMono(BookRequest.class)
                .flatMap(r -> Mono.zip(Mono.just(r), bookRepository.findById(request.pathVariable("id")),
                                authorRepository.findById(r.getAuthorId()),
                                genreRepository.findByIdIn(r.getGenresId()).collectList()))
                .flatMap(t -> bookRepository.save(new Book(t.getT2().getId(), t.getT1().getTitle(),
                        t.getT2().getTime(), t.getT3(), t.getT4(), t.getT2().getInfo())))
                .hasElement()
                .flatMap(b -> Mono.just(new ResultResponse(b)))
                .flatMap(r -> ServerResponse.ok().bodyValue(r));
    }

    @Override
    public Mono<ServerResponse> deleteBook(ServerRequest request){
        return bookRepository.findById(request.pathVariable("id"))
                .flatMap(b -> bookRepository.delete(b).thenReturn(b))
                .hasElement()
                .map(ResultResponse::new)
                .flatMap(r -> ServerResponse.ok().bodyValue(r));
    }

    private Mono<BookListRequest> getRequest(ServerRequest request){
        return Mono.just(request).flatMap(r -> Mono.zip(
                Mono.just(Integer.parseInt(request.queryParam("pageNumber").orElseThrow(() -> new NoSuchElementException("Missing query parameter pageNumber")))),
                Mono.just(Integer.parseInt(request.queryParam("pageSize").orElseThrow(() -> new NoSuchElementException("Missing query parameter pageSize")))),
                Mono.just(request.queryParam("sort").orElseThrow(() -> new NoSuchElementException("Missing query parameter sort")))))
                .map(z -> new BookListRequest(z.getT1(), z.getT2(), z.getT3()));
    }

    private Mono<String> getGenreId(ServerRequest request){
        return Mono.just(request.queryParam("genreId").orElseThrow(() -> new NoSuchElementException("Missing query parameter genreId")));
    }

    private Mono<String> getQuery(ServerRequest request){
        return Mono.just(request.queryParam("query").orElseThrow(() -> new NoSuchElementException("Missing query parameter query")));
    }

    private PageRequest getPageRequest(BookListRequest request){
        return PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(BookSort.valueOf(request.getSort()).getField()).descending());
    }

    private int getTotalPages(long count, int pageSize){
        return pageSize == 0 ? 0 : (int) Math.ceil(((double) count) / pageSize);
    }

}
