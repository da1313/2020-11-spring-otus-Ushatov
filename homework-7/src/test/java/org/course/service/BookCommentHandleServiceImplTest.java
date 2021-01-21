package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.domain.Book;
import org.course.domain.BookComment;
import org.course.domain.User;
import org.course.repository.BookCommentRepository;
import org.course.repository.BookRepository;
import org.course.service.BookCommentHandleServiceImpl;
import org.course.service.intefaces.BookCommentHandleService;
import org.course.service.intefaces.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@DisplayName("Class BookCommentHandleServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookCommentHandleServiceImplTest {

    public static final String BOOK_COMMENT_TEXT = "TEXT";
    public static final long BOOK_COMMENT_ID = 1L;
    public static final long BOOK_ID = 1L;
    public static final long BOOK_COMMENT_COUNT = 7L;
    public static final long USER_ID = 1L;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 3;
    @Mock
    private BookCommentRepository bookCommentRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<BookComment> bookCommentArgumentCaptor;

    @Test
    void shouldPassCorrectCommentToCreateMethod() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        book.setId(BOOK_ID);
        bookComment.setBook(book);
        User user = new User();
        bookComment.setText(BOOK_COMMENT_TEXT);
        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        service.createBookComment(BOOK_ID, USER_ID, BOOK_COMMENT_TEXT);
        Mockito.verify(bookCommentRepository).save(bookCommentArgumentCaptor.capture());
        Assertions.assertThat(bookCommentArgumentCaptor.getValue()).isEqualTo(bookComment);
    }

    @Test
    void shouldThrowExceptionIfEntityNotFound() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        Assertions.assertThatThrownBy(() -> service.createBookComment(BOOK_ID, USER_ID, BOOK_COMMENT_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + BOOK_ID + " not found!");
        Assertions.assertThatThrownBy(() -> service.readBookComment(BOOK_COMMENT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Comment with id " + BOOK_COMMENT_ID + " not found!");
        Assertions.assertThatThrownBy(() -> service.updateBookComment(BOOK_COMMENT_ID, BOOK_COMMENT_TEXT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Comment with id " + BOOK_COMMENT_ID + " not found!");
        Assertions.assertThatThrownBy(() -> service.deleteBookComment(BOOK_COMMENT_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Comment with id " + BOOK_COMMENT_ID + " not found!");
    }

    @Test
    void shouldPassCorrectAIdToMethod() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        bookComment.setId(BOOK_COMMENT_ID);
        bookComment.setText(BOOK_COMMENT_TEXT);
        bookComment.setBook(book);
        Mockito.when(bookCommentRepository.findWithBookAndUserById(BOOK_COMMENT_ID)).thenReturn(Optional.of(bookComment));
        service.readBookComment(BOOK_COMMENT_ID);
        Mockito.verify(bookCommentRepository).findWithBookAndUserById(longArgumentCaptor.capture());
        Assertions.assertThat(longArgumentCaptor.getValue()).isEqualTo(BOOK_COMMENT_ID);
    }

    @Test
    void shouldInvokeCorrectMethod() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        service.readAllBookComments(PAGE_NUMBER, PAGE_SIZE);
        Mockito.verify(bookCommentRepository, Mockito.times(1)).findAllWithBookAndUser(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
    }

    @Test
    void updateBookComment() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        bookComment.setId(BOOK_COMMENT_ID);
        bookComment.setText(BOOK_COMMENT_TEXT);
        bookComment.setBook(book);
        Mockito.when(bookCommentRepository.findById(BOOK_COMMENT_ID)).thenReturn(Optional.of(bookComment));
        service.updateBookComment(BOOK_COMMENT_ID, BOOK_COMMENT_TEXT);
        Mockito.verify(bookCommentRepository).save(bookCommentArgumentCaptor.capture());
        Assertions.assertThat(bookCommentArgumentCaptor.getValue()).isEqualTo(bookComment);
    }

    @Test
    void deleteBookComment() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        bookComment.setId(BOOK_COMMENT_ID);
        bookComment.setText(BOOK_COMMENT_TEXT);
        bookComment.setBook(book);
        Mockito.when(bookCommentRepository.findById(BOOK_COMMENT_ID)).thenReturn(Optional.of(bookComment));
        service.deleteBookComment(BOOK_COMMENT_ID);
        Mockito.verify(bookCommentRepository).delete(bookCommentArgumentCaptor.capture());
        Assertions.assertThat(bookCommentArgumentCaptor.getValue()).isEqualTo(bookComment);
    }

    @Test
    void deleteAllBookComments() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        service.deleteAllBookComments();
        Mockito.verify(bookCommentRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    void countBookComments() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentRepository, bookRepository, userRepository);
        Mockito.when(bookCommentRepository.count()).thenReturn(BOOK_COMMENT_COUNT);
        String count = service.countBookComments();
        Assertions.assertThat(count).isEqualTo(String.valueOf(BOOK_COMMENT_COUNT));
    }
}