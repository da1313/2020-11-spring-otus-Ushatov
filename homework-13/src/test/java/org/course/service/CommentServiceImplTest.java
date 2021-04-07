package org.course.service;

import liquibase.pro.packaged.M;
import org.assertj.core.api.Assertions;
import org.course.api.request.CommentRequest;
import org.course.domain.*;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.service.interfaces.CommentService;
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
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class CommentServiceImpl")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    public static final String TEXT = "T";
    public static final long BOOK_ID = 1L;
    public static final String TITLE = "T";
    public static final String DESCRIPTION = "D";

    @Mock
    private CommentRepository commentRepository;

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
    void shouldCreateCommentAndUpdateCommentCount() {

        CommentService commentService = new CommentServiceImpl(commentRepository, bookRepository);
        CommentRequest request = new CommentRequest(BOOK_ID, TEXT);
        User user = new User();
        Book book = new Book(0, TITLE, DESCRIPTION, LocalDateTime.now(), new Author(), new HashSet<>(), new Info());
        Comment expectedComment = new Comment(0, TEXT, null, book, user);
        Book expectedBook = new Book(book.getId(), book.getTitle(), book.getDescription(), book.getTime(),
                book.getAuthor(), book.getGenres(), book.getInfo());
        expectedBook.getInfo().setCommentCount(book.getInfo().getCommentCount() + 1);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        ArgumentCaptor<Book> bookArgumentCaptor  = ArgumentCaptor.forClass(Book.class);

        commentService.createComment(request);

        Mockito.verify(commentRepository).save(commentArgumentCaptor.capture());
        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());

        Comment actualComment = commentArgumentCaptor.getValue();
        expectedComment.setTime(actualComment.getTime());
        Book actualBook = bookArgumentCaptor.getValue();

        Assertions.assertThat(actualComment).isEqualTo(expectedComment);

        Assertions.assertThat(actualBook).isEqualTo(expectedBook);

    }
}