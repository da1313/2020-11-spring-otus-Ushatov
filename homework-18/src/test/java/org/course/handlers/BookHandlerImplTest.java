package org.course.handlers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.course.api.pojo.BookCount;
import org.course.api.pojo.BookShort;
import org.course.api.requests.BookRequest;
import org.course.api.responces.BookInfoResponse;
import org.course.api.responces.BookListResponse;
import org.course.api.responces.ResultResponse;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.embedded.BookSort;
import org.course.domain.embedded.Info;
import org.course.repositories.AuthorRepository;
import org.course.repositories.BookRepository;
import org.course.repositories.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Class BookHandlerImpl")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BookHandlerImplTest {

    public static final long COUNT = 6L;
    public static final int PAGE = 0;
    public static final int SIZE = 1;
    public static final String SORT = "NEW";
    public static final String GENRE_ID = "1";
    public static final long TOTAL_PAGES = (int) Math.ceil(((double) COUNT) / SIZE);
    public static final String QUERY = "query";
    public static final String BOOK_ID = "1";
    public static final String TITLE = "T";
    public static final String AUTHOR_ID = "1";
    public static final List<String> GENRES_ID = List.of("1", "2");
    public static final String NAME = "N";

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private WebTestClient client;

    @Test
    void shouldReturnBooksResponse() throws JsonProcessingException {

        List<BookShort> bookShortList = List.of(new BookShort(), new BookShort());

        BookListResponse expected = new BookListResponse(bookShortList, TOTAL_PAGES);

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(expected);

        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by(BookSort.valueOf(SORT).getField()).descending());

        Mockito.when(bookRepository.findAllBookShortAuto(pageRequest))
                .thenReturn(Flux.just(new BookShort(), new BookShort()));

        Mockito.when(bookRepository.count()).thenReturn(Mono.just(COUNT));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .queryParam("pageNumber", PAGE)
                        .queryParam("pageSize", SIZE)
                        .queryParam("sort", SORT).build())
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

    }

    @Test
    void shouldReturnBooksByGenreResponse() throws JsonProcessingException {

        List<BookShort> bookShortList = List.of(new BookShort(), new BookShort());

        BookListResponse expected = new BookListResponse(bookShortList, TOTAL_PAGES);

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(expected);

        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by(BookSort.valueOf(SORT).getField()).descending());

        Mockito.when(bookRepository.findAllBookShortByGenre(GENRE_ID, pageRequest))
                .thenReturn(Flux.just(new BookShort(), new BookShort()));

        Mockito.when(bookRepository.findCountByGenres(GENRE_ID)).thenReturn(Mono.just(new BookCount(COUNT)));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .queryParam("pageNumber", PAGE)
                        .queryParam("pageSize", SIZE)
                        .queryParam("sort", SORT)
                        .queryParam("genreId", GENRE_ID).build())
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

    }

    @Test
    void shouldReturnBooksByQueryResponse() throws JsonProcessingException {

        List<BookShort> bookShortList = List.of(new BookShort(), new BookShort());

        BookListResponse expected = new BookListResponse(bookShortList, TOTAL_PAGES);

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(expected);

        PageRequest pageRequest = PageRequest.of(PAGE, SIZE, Sort.by(BookSort.valueOf(SORT).getField()).descending());

        Mockito.when(bookRepository.findAllBookShortByQuery(QUERY, pageRequest))
                .thenReturn(Flux.just(new BookShort(), new BookShort()));

        Mockito.when(bookRepository.findCountByQuery(QUERY)).thenReturn(Mono.just(new BookCount(COUNT)));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .queryParam("pageNumber", PAGE)
                        .queryParam("pageSize", SIZE)
                        .queryParam("sort", SORT)
                        .queryParam("query", QUERY).build())
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

    }

    @Test
    void shouldReturnBookByIdResponse() throws JsonProcessingException {

        Book book = new Book(BOOK_ID, null, null, null, null, new Info());

        BookInfoResponse response = BookInfoResponse.ofBook(book);

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(response);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Mono.just(book));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/" + BOOK_ID).build())
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

    }

    @Test
    void shouldCreateBookAndReturnTrueOnSuccess() throws JsonProcessingException {

        BookRequest request = new BookRequest(TITLE, AUTHOR_ID, GENRES_ID);

        ResultResponse response = new ResultResponse(true);

        Author author = new Author(AUTHOR_ID, NAME);

        List<Genre> genreList = List.of(new Genre(GENRES_ID.get(0), NAME), new Genre(GENRES_ID.get(1), NAME));

        Book book = new Book(null, TITLE, null, author, genreList, new Info());

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(response);

        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Mono.just(author));

        Mockito.when(genreRepository.findByIdIn(GENRES_ID)).thenReturn(Flux.just(genreList.get(0), genreList.get(1)));

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(Mono.just(book));

        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Book actual = bookArgumentCaptor.getValue();

        book.setTime(actual.getTime());

        Assertions.assertThat(actual).isEqualTo(book);

    }

    @Test
    void shouldUpdateBookAndReturnTrueOnSuccess() throws JsonProcessingException {

        BookRequest request = new BookRequest(TITLE, AUTHOR_ID, GENRES_ID);

        ResultResponse response = new ResultResponse(true);

        Author author = new Author(AUTHOR_ID, NAME);

        List<Genre> genreList = List.of(new Genre(GENRES_ID.get(0), NAME), new Genre(GENRES_ID.get(1), NAME));

        Book bookBefore = new Book(BOOK_ID, null, LocalDateTime.now(), null, null, new Info());

        Book bookAfter = new Book(BOOK_ID, TITLE, bookBefore.getTime(), author, genreList, new Info());

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(response);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Mono.just(bookBefore));

        Mockito.when(authorRepository.findById(AUTHOR_ID)).thenReturn(Mono.just(author));

        Mockito.when(genreRepository.findByIdIn(GENRES_ID)).thenReturn(Flux.just(genreList.get(0), genreList.get(1)));

        Mockito.when(bookRepository.save(bookAfter)).thenReturn(Mono.just(bookAfter));

        client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/" + BOOK_ID)
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Book actual = bookArgumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(bookAfter);

    }

    @Test
    void shouldDeleteBookAndReturnTrueOnSuccess() throws JsonProcessingException {

        ResultResponse response = new ResultResponse(true);

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(response);

        Book book = new Book(BOOK_ID, null, LocalDateTime.now(), null, null, new Info());

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Mono.just(book));

        Mockito.when(bookRepository.delete(book)).thenReturn(Mono.empty());

        client.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/" + BOOK_ID)
                        .build())
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

    }

}