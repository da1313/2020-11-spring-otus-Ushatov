package org.course.service;

import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@DisplayName("Class BookHandlerServiceImpl")
@ExtendWith(MockitoExtension.class)
class BookHandlerServiceImplTest {

    public static final long AUTHOR_ID = 1L;
    public static final long GENRE_ID = 1L;
    public static final String AUTHOR_NAME = "A1";
    public static final String GENRE_NAME = "G1";
    public static final String BOOK_NAME = "B1";
    public static final long BOOK_ID = 1L;
    public static final long BOOK_COUNT = 1L;
    public static final long CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "C1";
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private CategoryDao categoryDao;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;
    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID)));
        Assertions.assertEquals("Author with id " + AUTHOR_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfGenreNotFoundOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(new Author(AUTHOR_ID, AUTHOR_NAME)));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID)));
        Assertions.assertEquals("Genre with id " + GENRE_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfIdParseErrorOnCreate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.createBook(BOOK_NAME, "1a", String.valueOf(GENRE_ID)));
        Assertions.assertEquals("Parse error! For input string: \"1a\"", exception.getMessage());
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnCreate(){
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME);
        Genre genre = new Genre(GENRE_ID, GENRE_NAME);
        Book book = new Book(BOOK_NAME, author, genre);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        service.createBook(BOOK_NAME, String.valueOf(AUTHOR_ID), String.valueOf(GENRE_ID));
        Mockito.verify(bookDao).create(bookArgumentCaptor.capture());
        Assertions.assertEquals(book, bookArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnReadById() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.readBook(BOOK_ID));
        Assertions.assertEquals("Book with id " + BOOK_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnUpdate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME, GENRE_ID, GENRE_NAME));
        Assertions.assertEquals("Book with id " + BOOK_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfAuthorNotFoundOnUpdate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.empty());
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME, GENRE_ID, GENRE_NAME));
        Assertions.assertEquals("Author with id " + AUTHOR_ID + " not found!", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionIfGenreNotFoundOnUpdate() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(new Author()));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.empty());
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME, GENRE_ID, GENRE_NAME));
        Assertions.assertEquals("Genre with id " + GENRE_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnUpdate(){
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME);
        Genre genre = new Genre(GENRE_ID, GENRE_NAME);
        Book book = new Book(BOOK_NAME, author, genre);
        Mockito.when(authorDao.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        Mockito.when(genreDao.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        service.updateBook(BOOK_ID, BOOK_NAME, AUTHOR_ID, AUTHOR_NAME, GENRE_ID, GENRE_NAME);
        Mockito.verify(bookDao).update(bookArgumentCaptor.capture());
        Assertions.assertEquals(book, bookArgumentCaptor.getValue());
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnDelete() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        service.deleteBook(BOOK_ID);
        Mockito.verify(bookDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(BOOK_ID, longArgumentCaptor.getValue());
    }

    @Test
    void shouldInvokeCorrectDaoMethodOnDelete() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        service.deleteAllBooks();
        Mockito.verify(bookDao, Mockito.times(1)).delete();
    }

    @Test
    void shouldReturnCorrectBookCount() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.count()).thenReturn(BOOK_COUNT);
        String actual = service.countBooks();
        Assertions.assertEquals(String.valueOf(BOOK_COUNT), actual);
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnAddCategory() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.addCategory(BOOK_ID, CATEGORY_ID));
        Assertions.assertEquals("Book with id " + BOOK_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCategoryNotFoundOnAddCategory() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.addCategory(BOOK_ID, CATEGORY_ID));
        Assertions.assertEquals("Category with id " + CATEGORY_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnAddCategory() {
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME);
        Genre genre = new Genre(GENRE_ID, GENRE_NAME);
        Category category = new Category(CATEGORY_ID, CATEGORY_NAME);
        Book book = new Book(BOOK_NAME, author, genre);
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        service.addCategory(BOOK_ID, CATEGORY_ID);
        Mockito.verify(bookDao).addCategory(bookArgumentCaptor.capture(), categoryArgumentCaptor.capture());
        Assertions.assertAll(
                () -> Assertions.assertEquals(book, bookArgumentCaptor.getValue()),
                () -> Assertions.assertEquals(category, categoryArgumentCaptor.getValue())
                );
    }


    @Test
    void shouldThrowExceptionIfBookNotFoundOnRemoveCategory() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.removeCategory(BOOK_ID, CATEGORY_ID));
        Assertions.assertEquals("Book with id " + BOOK_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCategoryNotFoundOnRemoveCategory() {
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.removeCategory(BOOK_ID, CATEGORY_ID));
        Assertions.assertEquals("Category with id " + CATEGORY_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnRemoveCategory() {
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME);
        Genre genre = new Genre(GENRE_ID, GENRE_NAME);
        Category category = new Category(CATEGORY_ID, CATEGORY_NAME);
        Book book = new Book(BOOK_NAME, author, genre);
        BookHandlerServiceImpl service = new BookHandlerServiceImpl(bookDao, authorDao, genreDao, categoryDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        service.removeCategory(BOOK_ID, CATEGORY_ID);
        Mockito.verify(bookDao).removeCategory(bookArgumentCaptor.capture(), categoryArgumentCaptor.capture());
        Assertions.assertAll(
                () -> Assertions.assertEquals(book, bookArgumentCaptor.getValue()),
                () -> Assertions.assertEquals(category, categoryArgumentCaptor.getValue())
        );
    }
}