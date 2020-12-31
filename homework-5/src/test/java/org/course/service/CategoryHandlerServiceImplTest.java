package org.course.service;

import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;
import org.course.service.intefaces.CategoryHandlerService;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Class CategoryHandlerServiceImpl")
@ExtendWith(MockitoExtension.class)
class CategoryHandlerServiceImplTest {
    public static final long AUTHOR_ID = 1L;
    public static final long GENRE_ID = 1L;
    public static final String AUTHOR_NAME = "A1";
    public static final String GENRE_NAME = "G1";
    public static final String BOOK_NAME = "B1";
    public static final long CATEGORY_COUNT = 1;
    public static final long BOOK_ID = 1L;
    public static final long CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "C1";
    @Mock
    private BookDao bookDao;
    @Mock
    private CategoryDao categoryDao;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;
    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void shouldPassCorrectCategoryToCreateMethod() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Category category = new Category(CATEGORY_NAME);
        service.createCategory(CATEGORY_NAME);
        Mockito.verify(categoryDao).create(categoryArgumentCaptor.capture());
        Assertions.assertEquals(category, categoryArgumentCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionIfCategoryNotFoundOnRead() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.readCategory(CATEGORY_ID));
        Assertions.assertEquals("Category with id " + CATEGORY_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCategoryNotFoundOnUpdate() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.updateCategory(CATEGORY_ID, CATEGORY_NAME));
        Assertions.assertEquals("Category with id " + CATEGORY_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldPassCorrectCategoryToUpdateMethod() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Category category = new Category(CATEGORY_ID, CATEGORY_NAME);
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        service.updateCategory(CATEGORY_ID, CATEGORY_NAME);
        Mockito.verify(categoryDao).update(categoryArgumentCaptor.capture());
        Assertions.assertEquals(category, categoryArgumentCaptor.getValue());
    }

    @Test
    void shouldPassCorrectCategoryToDeleteMethod() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        service.deleteCategory(CATEGORY_ID);
        Mockito.verify(categoryDao).deleteById(longArgumentCaptor.capture());
        Assertions.assertEquals(CATEGORY_ID, longArgumentCaptor.getValue());
    }

    @Test
    void deleteAllCategories() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        service.deleteAllCategories();
        Mockito.verify(categoryDao, Mockito.times(1)).delete();
    }

    @Test
    void countCategories() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(categoryDao.count()).thenReturn(CATEGORY_COUNT);
        String actual = service.countCategories();
        Assertions.assertEquals(String.valueOf(CATEGORY_COUNT), actual);
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnAddBook() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.addBook(CATEGORY_ID, BOOK_ID));
        Assertions.assertEquals("Book with id " + BOOK_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCategoryNotFoundOnAddBook() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.addBook(CATEGORY_ID, BOOK_ID));
        Assertions.assertEquals("Category with id " + CATEGORY_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnAddBook(){
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME);
        Genre genre = new Genre(GENRE_ID, GENRE_NAME);
        Category category = new Category(CATEGORY_ID, CATEGORY_NAME);
        Book book = new Book(BOOK_NAME, author, genre);
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        service.addBook(CATEGORY_ID, BOOK_ID);
        Mockito.verify(categoryDao).addBook(categoryArgumentCaptor.capture(), bookArgumentCaptor.capture());
        Assertions.assertAll(
                () -> Assertions.assertEquals(book, bookArgumentCaptor.getValue()),
                () -> Assertions.assertEquals(category, categoryArgumentCaptor.getValue())
        );
    }

    @Test
    void shouldPassCorrectArgumentsToDaoMethodOnRemoveCategory(){
        Author author = new Author(AUTHOR_ID, AUTHOR_NAME);
        Genre genre = new Genre(GENRE_ID, GENRE_NAME);
        Category category = new Category(CATEGORY_ID, CATEGORY_NAME);
        Book book = new Book(BOOK_NAME, author, genre);
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        service.removeBook(CATEGORY_ID, BOOK_ID);
        Mockito.verify(categoryDao).removeBook(categoryArgumentCaptor.capture(), bookArgumentCaptor.capture());
        Assertions.assertAll(
                () -> Assertions.assertEquals(book, bookArgumentCaptor.getValue()),
                () -> Assertions.assertEquals(category, categoryArgumentCaptor.getValue())
        );
    }

    @Test
    void shouldThrowExceptionIfBookNotFoundOnRemoveBook() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.removeBook(CATEGORY_ID, BOOK_ID));
        Assertions.assertEquals("Book with id " + BOOK_ID + " not found!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionIfCategoryNotFoundOnRemoveBook() {
        CategoryHandlerService service = new CategoryHandlerServiceImpl(categoryDao, bookDao);
        Mockito.when(bookDao.findById(BOOK_ID)).thenReturn(Optional.of(new Book()));
        Mockito.when(categoryDao.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.removeBook(CATEGORY_ID, BOOK_ID));
        Assertions.assertEquals("Category with id " + CATEGORY_ID + " not found!", exception.getMessage());
    }
}