package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.api.request.ScoreRequest;
import org.course.domain.*;
import org.course.repository.BookRepository;
import org.course.repository.ScoreRepository;
import org.course.service.interfaces.ScoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class ScoreServiceImpl")
@ExtendWith(MockitoExtension.class)
class ScoreServiceImplTest {

    public static final int VALUE = 5;
    public static final long BOOK_ID = 1L;
    private static final String TITLE = "T";
    private static final String DESCRIPTION = "D";
    public static final int OLD_VALUE = 3;
    public static final int SCORE_ID = 1;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic;

    @BeforeEach
    private void setUp(){
        securityContextHolderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class);
        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    private void tearDown(){
        securityContextHolderMockedStatic.close();
    }

    @Test
    void shouldCreateScoreAndUpdateBookInfoNoExistingScoreCase() {

        ScoreService service = new ScoreServiceImpl(scoreRepository, bookRepository);

        ScoreRequest request = new ScoreRequest(BOOK_ID, VALUE);

        Book book = new Book(0, TITLE, DESCRIPTION, LocalDateTime.now(), new Author(), new HashSet<>(), new Info());

        User user = new User();

        Score expectedScore = new Score(0, VALUE, user, book);
        Book expectedBook = new Book(book.getId(), book.getTitle(), book.getDescription(), book.getTime(),
                book.getAuthor(), book.getGenres(), book.getInfo());
        expectedBook.getInfo().addScore(VALUE);
        expectedBook.getInfo().setAvgScore();

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(scoreRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);


        ArgumentCaptor<Score> scoreArgumentCaptor = ArgumentCaptor.forClass(Score.class);
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        service.createScore(request);

        Mockito.verify(scoreRepository).save(scoreArgumentCaptor.capture());
        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Score actualScore = scoreArgumentCaptor.getValue();
        Book actualBook = bookArgumentCaptor.getValue();

        Assertions.assertThat(actualScore).isEqualTo(expectedScore);
        Assertions.assertThat(actualBook).isEqualTo(expectedBook);

    }

    @Test
    void shouldCreateScoreAndUpdateBookInfoExistingScoreCase() {

        ScoreService service = new ScoreServiceImpl(scoreRepository, bookRepository);

        ScoreRequest request = new ScoreRequest(BOOK_ID, VALUE);

        Book book = new Book(BOOK_ID, TITLE, DESCRIPTION, LocalDateTime.now(), new Author(), new HashSet<>(), new Info());

        User user = new User();

        Score score = new Score(SCORE_ID, OLD_VALUE, user, book);

        Score expectedScore = new Score(score.getId(), VALUE, score.getUser(), score.getBook());
        Book expectedBook = new Book(book.getId(), book.getTitle(), book.getDescription(), book.getTime(),
                book.getAuthor(), book.getGenres(), book.getInfo());
        expectedBook.getInfo().removeScore(OLD_VALUE);
        expectedBook.getInfo().addScore(VALUE);
        expectedBook.getInfo().setAvgScore();

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(scoreRepository.findByUserAndBook(user, book)).thenReturn(Optional.of(score));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);


        ArgumentCaptor<Score> scoreArgumentCaptor = ArgumentCaptor.forClass(Score.class);
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        service.createScore(request);

        Mockito.verify(scoreRepository).save(scoreArgumentCaptor.capture());
        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Score actualScore = scoreArgumentCaptor.getValue();
        Book actualBook = bookArgumentCaptor.getValue();

        Assertions.assertThat(actualScore).isEqualTo(expectedScore);
        Assertions.assertThat(actualBook).isEqualTo(expectedBook);

    }
}