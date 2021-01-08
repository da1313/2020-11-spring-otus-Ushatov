package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.dao.interfaces.BookCommentDao;
import org.course.dao.interfaces.BookDao;
import org.course.domain.Book;
import org.course.domain.BookComment;
import org.course.service.intefaces.BookCommentHandleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@DisplayName("Class BookCommentHandleServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookCommentHandleServiceImplTest {

    public static final String BOOK_COMMENT_TEXT = "TEXT";
    public static final long BOOK_COMMENT_ID = 1L;
    public static final long BOOK_ID = 1L;
    public static final long BOOK_COMMENT_COUNT = 7L;
    @Mock
    private BookCommentDao bookCommentDao;
    @Mock
    private BookDao bookDao;
    @Captor
    private ArgumentCaptor<BookComment> bookCommentArgumentCaptor;

    @Test
    void shouldPassCorrectCommentToCreateMethod() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        book.setId(BOOK_ID);
        bookComment.setBook(book);
        bookComment.setText(BOOK_COMMENT_TEXT);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        service.createBookComment(BOOK_ID, BOOK_COMMENT_TEXT);
        Mockito.verify(bookCommentDao).save(bookCommentArgumentCaptor.capture());
        Assertions.assertThat(bookCommentArgumentCaptor.getValue()).isEqualTo(bookComment);
    }

    @Test
    void shouldThrowExceptionIfEntityNotFound() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        Assertions.assertThatThrownBy(() -> service.createBookComment(BOOK_ID, BOOK_COMMENT_TEXT))
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
    void readBookComment() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        bookComment.setId(BOOK_COMMENT_ID);
        bookComment.setText(BOOK_COMMENT_TEXT);
        bookComment.setBook(book);
        Mockito.when(bookCommentDao.findById(BOOK_COMMENT_ID)).thenReturn(Optional.of(bookComment));
        service.readBookComment(BOOK_COMMENT_ID);
        Mockito.verify(bookCommentDao).findById(longArgumentCaptor.capture());
        Assertions.assertThat(longArgumentCaptor.getValue()).isEqualTo(BOOK_COMMENT_ID);
    }

    @Test
    void readAllBookComments() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        service.readAllBookComments();
        Mockito.verify(bookCommentDao, Mockito.times(1)).findAll();
    }

    @Test
    void updateBookComment() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        bookComment.setId(BOOK_COMMENT_ID);
        bookComment.setText(BOOK_COMMENT_TEXT);
        bookComment.setBook(book);
        Mockito.when(bookCommentDao.findById(BOOK_COMMENT_ID)).thenReturn(Optional.of(bookComment));
        service.updateBookComment(BOOK_COMMENT_ID, BOOK_COMMENT_TEXT);
        Mockito.verify(bookCommentDao).save(bookCommentArgumentCaptor.capture());
        Assertions.assertThat(bookCommentArgumentCaptor.getValue()).isEqualTo(bookComment);
    }

    @Test
    void deleteBookComment() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        BookComment bookComment = new BookComment();
        Book book = new Book();
        bookComment.setId(BOOK_COMMENT_ID);
        bookComment.setText(BOOK_COMMENT_TEXT);
        bookComment.setBook(book);
        Mockito.when(bookCommentDao.findById(BOOK_COMMENT_ID)).thenReturn(Optional.of(bookComment));
        service.deleteBookComment(BOOK_COMMENT_ID);
        Mockito.verify(bookCommentDao).delete(bookCommentArgumentCaptor.capture());
        Assertions.assertThat(bookCommentArgumentCaptor.getValue()).isEqualTo(bookComment);
    }

    @Test
    void deleteAllBookComments() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        service.deleteAllBookComments();
        Mockito.verify(bookCommentDao, Mockito.times(1)).deleteAll();
    }

    @Test
    void countBookComments() {
        BookCommentHandleService service = new BookCommentHandleServiceImpl(bookCommentDao, bookDao);
        Mockito.when(bookCommentDao.count()).thenReturn(BOOK_COMMENT_COUNT);
        String count = service.countBookComments();
        Assertions.assertThat(count).isEqualTo(String.valueOf(BOOK_COMMENT_COUNT));
    }
}