package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.domain.*;
import org.course.repository.BookRepository;
import org.course.repository.CardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Class BookServiceImpl")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BookServiceImplTest {

    public static final Book BOOK1 = new Book("1", "t", new Author("1", "a"), new Genre("1", "g"), 2);
    public static final Book BOOK2 = new Book("2", "t", new Author("1", "a"), new Genre("1", "g"), 2);
    public static final User USER = new User("1", "u", "p");
    public static final Card CARD = new Card("1", USER, BOOK1, LocalDateTime.now(), LocalDateTime.now());

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CardRepository cardRepository;

    @Autowired
    private RepositoryEntityLinks repositoryEntityLinks;

    private MockedStatic<SecurityContextHolder> context;

    @BeforeEach
    void mockContext(){
        context = Mockito.mockStatic(SecurityContextHolder.class);
        context.when(SecurityContextHolder::getContext)
                .thenReturn(new SecurityContextImpl(new UsernamePasswordAuthenticationToken(USER, USER.getPassword())));
    }

    @AfterEach
    void deregisterMock(){
        context.close();
    }

    @Test
    void shouldReturnBooksAndDoNotAddTakeLinkToAlreadyTakenBook() {

        new SecurityContextImpl(new UsernamePasswordAuthenticationToken("1", "1"));

        BookServiceImpl service = new BookServiceImpl(bookRepository, cardRepository, repositoryEntityLinks);

        Mockito.when(bookRepository.findAll()).thenReturn(List.of(BOOK1, BOOK2));
        Mockito.when(cardRepository.findByUser(USER)).thenReturn(List.of(CARD));

        CollectionModel<EntityModel<Book>> books = service.getBooks();

        EntityModel<Book> bookEntityModel = books.getContent()
                .stream().filter(b -> b.getContent().getId().equals(BOOK1.getId())).findFirst().orElseThrow();

        Assertions.assertThat(bookEntityModel).extracting(e -> e.hasLink("return")).isEqualTo(true);
        Assertions.assertThat(bookEntityModel).extracting(e -> e.hasLink("take")).isEqualTo(false);

    }

    @Test
    void shouldReturnBooksAndAddReturnLinkToAlreadyTakenBook() {

        new SecurityContextImpl(new UsernamePasswordAuthenticationToken("1", "1"));

        BookServiceImpl service = new BookServiceImpl(bookRepository, cardRepository, repositoryEntityLinks);

        Mockito.when(bookRepository.findAll()).thenReturn(List.of(BOOK1, BOOK2));
        Mockito.when(cardRepository.findByUser(USER)).thenReturn(List.of(CARD));

        CollectionModel<EntityModel<Book>> books = service.getBooks();

        EntityModel<Book> bookEntityModel = books.getContent().stream()
                .filter(b -> b.getContent().getId().equals(BOOK1.getId())).findFirst().orElseThrow();

        Assertions.assertThat(bookEntityModel).extracting(e -> e.hasLink("return")).isEqualTo(true);
        Assertions.assertThat(bookEntityModel).extracting(e -> e.hasLink("take")).isEqualTo(false);

    }
}