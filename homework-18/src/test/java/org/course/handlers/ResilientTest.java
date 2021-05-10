package org.course.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.course.api.pojo.BookShort;
import org.course.repositories.AuthorRepository;
import org.course.repositories.BookRepository;
import org.course.repositories.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@DisplayName("Test resilient behavior")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class ResilientTest {

    public static final long COUNT = 6L;
    public static final int PAGE = 0;
    public static final int SIZE = 1;
    public static final String SORT = "NEW";

    @Autowired
    private WebTestClient client;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void shouldOpenCircuitBreakerOnBusinessExceptionNoRequestParametersSupplied() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(Collections.singletonMap("result", "false"));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books").build())
                .exchange()
                .expectStatus().isOk().expectBody().json(value);

    }

    @Test
    void shouldOpenCircuitBreakerOnExceptionRisenDuringDatabaseCall() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(Collections.singletonMap("result", "false"));

        Mockito.when(bookRepository.findAllBookShortAuto(Mockito.any())).thenThrow(new RuntimeException());

        Mockito.when(bookRepository.count()).thenThrow(new RuntimeException());

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
    void shouldOpenTimeLimiterBreakerOnResponseDelayOverThanSpecifiedInProperties() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        String value = mapper.writeValueAsString(Collections.singletonMap("result", "false"));

        Mockito.when(bookRepository.findAllBookShortAuto(Mockito.any(Pageable.class)))
                .thenAnswer(new AnswersWithDelay(4000, invocation -> Flux.just(new BookShort())));

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
}
