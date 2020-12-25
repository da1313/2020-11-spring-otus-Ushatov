package org.course.service;

import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;
import org.course.service.intefaces.UserInterfaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Class CreateEntityServiceImpl")
@ExtendWith(MockitoExtension.class)
class CreateEntityServiceImplTest {
    public static final String AUTHORS_TABLE = "authors";
    public static final int CATEGORY_COUNT = 3;
    public static final String BOOKS_TABLE = "books";
    public static final String GENRES_TABLE = "genres";
    public static final String CATEGORY_TABLE = "categories";
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private CategoryDao categoryDao;
    @Mock
    private UserInterfaceService userInterfaceService;
    @Captor
    private ArgumentCaptor<Author> authorArgumentCaptor;
    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;
    @Captor
    private ArgumentCaptor<Genre> genreArgumentCaptor;
    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    @Test
    public void shouldCreateAuthor(){
        Author author = new Author(1L, "New author");
        CreateEntityServiceImpl service = new CreateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getAuthor()).thenReturn(author);
        service.createEntity(AUTHORS_TABLE);
        Mockito.verify(authorDao).createAndIncrement(authorArgumentCaptor.capture());
        Assertions.assertEquals(author, authorArgumentCaptor.getValue());
    }

    @Test
    public void shouldCreateBook(){
        Author author = new Author(1L, "New author");
        Genre genre = new Genre(1L, "New genre");
        List<Category> categories = new ArrayList<>();
        for (long i = 1; i <= CATEGORY_COUNT ; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName("C" + i);
            categories.add(category);
        }
        Book book = new Book();
        book.setName("New book");
        book.setAuthor(author);
        book.setGenre(genre);
        book.setCategories(categories);
        Mockito.when(userInterfaceService.getAuthorByIdNullable()).thenReturn(author);
        Mockito.when(userInterfaceService.getGenreByIdNullable()).thenReturn(genre);
        Mockito.when(userInterfaceService.getBookName()).thenReturn("New book");
        Mockito.when(userInterfaceService.getCategories()).thenReturn(categories);
        CreateEntityServiceImpl service = new CreateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        service.createEntity(BOOKS_TABLE);
        Mockito.verify(bookDao).createAndIncrement(bookArgumentCaptor.capture());
        Assertions.assertEquals(book, bookArgumentCaptor.getValue());
    }

    @Test
    public void shouldCreateGenre(){
        Genre genre = new Genre(1L, "New genre");
        CreateEntityServiceImpl service = new CreateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getGenre()).thenReturn(genre);
        service.createEntity(GENRES_TABLE);
        Mockito.verify(genreDao).createAndIncrement(genreArgumentCaptor.capture());
        Assertions.assertEquals(genre, genreArgumentCaptor.getValue());
    }

    @Test
    public void shouldCreateCategory(){
        Category category = new Category();
        category.setId(1L);
        category.setName("New category");
        CreateEntityServiceImpl service = new CreateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getCategory()).thenReturn(category);
        service.createEntity(CATEGORY_TABLE);
        Mockito.verify(categoryDao).createAndIncrement(categoryArgumentCaptor.capture());
        Assertions.assertEquals(category, categoryArgumentCaptor.getValue());
    }
}