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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class UpdateEntityServiceImpl")
@ExtendWith(MockitoExtension.class)
class UpdateEntityServiceImplTest {
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

    private ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

    @Test
    void shouldUpdateAuthors(){
        Author before = new Author(1L, "Old author");
        Author after = new Author(1L, "New author");
        UpdateEntityServiceImpl service = new UpdateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getAuthorById()).thenReturn(before);
        Mockito.when(userInterfaceService.getAuthorName()).thenReturn("New author");
        service.updateEntity(AUTHORS_TABLE);
        Mockito.verify(authorDao).update(authorArgumentCaptor.capture());
        assertEquals(after, authorArgumentCaptor.getValue());
    }

    @Test
    void shouldUpdateGenres(){
        Genre before = new Genre(1L, "Old genre");
        Genre after = new Genre(1L, "New genre");
        UpdateEntityServiceImpl service = new UpdateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getGenreById()).thenReturn(before);
        Mockito.when(userInterfaceService.getGenreName()).thenReturn("New genre");
        service.updateEntity(GENRES_TABLE);
        Mockito.verify(genreDao).update(genreArgumentCaptor.capture());
        assertEquals(after, genreArgumentCaptor.getValue());
    }

    @Test
    void shouldUpdateCategory(){
        Category before = new Category(1L, "Old category");
        Category after = new Category(1L, "New category");
        UpdateEntityServiceImpl service = new UpdateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Mockito.when(userInterfaceService.getCategoryById()).thenReturn(before);
        Mockito.when(userInterfaceService.getCategoryName()).thenReturn("New category");
        service.updateEntity(CATEGORY_TABLE);
        Mockito.verify(categoryDao).update(categoryArgumentCaptor.capture());
        assertEquals(after, categoryArgumentCaptor.getValue());
    }

    @Test
    void shouldUpdateBook(){
        UpdateEntityServiceImpl service = new UpdateEntityServiceImpl(bookDao, authorDao, genreDao, categoryDao, userInterfaceService);
        Author author = new Author(1L, "Old author");
        Genre genre = new Genre(1L, "Old genre");
        List<Category> categories = new ArrayList<>();
        categories.add(null);
        for (long i = 1; i <= CATEGORY_COUNT ; i++) {
            Category category = new Category(i, "C" + i);
            categories.add(category);
        }
        Book before = new Book("Old book", author, genre, List.of(categories.get(1), categories.get(2), categories.get(3)));
        Author author1 = new Author(2L, "New author");
        Genre genre1 = new Genre(3L, "New genre");
        Book after = new Book("New book", author1, genre1, List.of(categories.get(1)));
        Mockito.when(userInterfaceService.getBookById()).thenReturn(before);
        Mockito.when(userInterfaceService.getBookName("Old book")).thenReturn("New book");
        Mockito.when(userInterfaceService.getAuthorByIdNullable(author)).thenReturn(author1);
        Mockito.when(userInterfaceService.getGenreByIdNullable(genre)).thenReturn(genre1);
        Mockito.when(userInterfaceService.getCategories(List.of(categories.get(1), categories.get(2), categories.get(3)))).thenReturn(List.of(categories.get(1)));
        service.updateEntity(BOOKS_TABLE);
        Mockito.verify(bookDao).update(bookArgumentCaptor.capture());
        assertEquals(after, bookArgumentCaptor.getValue());
    }
}