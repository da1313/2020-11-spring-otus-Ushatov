package org.course.service;

import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Category;
import org.course.domain.Genre;
import org.course.service.intefaces.PrintService;
import org.course.service.intefaces.UserInterfaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class UserInterfaceServiceImpl")
@ExtendWith(MockitoExtension.class)
class UserInterfaceServiceImplTest {

    public static final int CATEGORY_COUNT = 3;
    @Mock
    private BookDao bookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private GenreDao genreDao;
    @Mock
    private CategoryDao categoryDao;
    @Mock
    private PrintService printService;

    @Test
    void shouldParseEntityIdCorrectly() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("15653".getBytes()));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        long actual = service.getEntityId("type");
        assertEquals(15653L, actual);
    }

    @Test
    void shouldReturnAuthorByIdNullableNotNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("1".getBytes()));
        Optional<Author> author = Optional.of(new Author(1L, "author"));
        Mockito.when(authorDao.findById(1L)).thenReturn(author);
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Author actual = service.getAuthorByIdNullable();
        assertEquals(author.get(), actual);
    }

    @Test
    void shouldReturnAuthorByIdNullableNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("null".getBytes()));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Author actual = service.getAuthorByIdNullable();
        assertNull(actual);
    }

    @Test
    void shouldReturnAuthorByIdNullableParametrizedOldAuthorCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("\r".getBytes()));
        Optional<Author> author = Optional.of(new Author(1L, "author"));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Author actual = service.getAuthorByIdNullable(author.get());
        assertEquals(author.get(), actual);
    }

    @Test
    void shouldReturnAuthorByIdNullableParametrizedNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("null".getBytes()));
        Optional<Author> author = Optional.of(new Author(1L, "author"));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Author actual = service.getAuthorByIdNullable(author.get());
        assertNull(actual);
    }

    @Test
    void shouldReturnAuthorByIdNullableParametrizedNotNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("2".getBytes()));
        Optional<Author> oldAuthor = Optional.of(new Author(1L, "Old author"));
        Optional<Author> author = Optional.of(new Author(2L, "author"));
        Mockito.when(authorDao.findById(2L)).thenReturn(author);
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Author actual = service.getAuthorByIdNullable(oldAuthor.get());
        assertEquals(author.get(), actual);
    }

    @Test
    void shouldReturnAuthorById() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("1".getBytes()));
        Optional<Author> author = Optional.of(new Author(1L, "author"));
        Mockito.when(authorDao.findById(1L)).thenReturn(author);
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Author actual = service.getAuthorById();
        assertEquals(author.get(), actual);
    }

    @Test
    void shouldReturnGenreByIdNullableNotNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("1".getBytes()));
        Optional<Genre> genre = Optional.of(new Genre(1L, "genre"));
        Mockito.when(genreDao.findById(1L)).thenReturn(genre);
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Genre actual = service.getGenreByIdNullable();
        assertEquals(genre.get(), actual);
    }

    @Test
    void shouldReturnGenreByIdNullableNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("null".getBytes()));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Genre actual = service.getGenreByIdNullable();
        assertNull(actual);
    }

    @Test
    void shouldReturnGenreByIdNullableParametrizedNotNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("2".getBytes()));
        Optional<Genre> oldGenre = Optional.of(new Genre(1L, "Old genre"));
        Optional<Genre> genre = Optional.of(new Genre(2L, "genre"));
        Mockito.when(genreDao.findById(2L)).thenReturn(genre);
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Genre actual = service.getGenreByIdNullable(oldGenre.get());
        assertEquals(genre.get(), actual);
    }

    @Test
    void shouldReturnGenreByIdNullableParametrizedOldGenreCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("\r".getBytes()));
        Optional<Genre> oldGenre = Optional.of(new Genre(1L, "Old genre"));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Genre actual = service.getGenreByIdNullable(oldGenre.get());
        assertEquals(oldGenre.get(), actual);
    }

    @Test
    void shouldReturnGenreByIdNullableParametrizedNullCase() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("null".getBytes()));
        Optional<Genre> oldGenre = Optional.of(new Genre(1L, "Old genre"));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Genre actual = service.getGenreByIdNullable(oldGenre.get());
        assertNull(actual);
    }

    @Test
    void shouldReturnGenreById() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("1".getBytes()));
        Optional<Genre> genre = Optional.of(new Genre(1L, "genre"));
        Mockito.when(genreDao.findById(1L)).thenReturn(genre);
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Genre actual = service.getGenreById();
        assertEquals(genre.get(), actual);
    }

    @Test
    void shouldReturnCategoryById() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("1".getBytes()));
        Optional<Category> category = Optional.of(new Category(1L, "category"));
        Mockito.when(categoryDao.findById(1L)).thenReturn(category);
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        Category actual = service.getCategoryById();
        assertEquals(category.get(), actual);
    }

    @Test
    void shouldReturnCategoryList() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("1,2,3".getBytes()));
        List<Optional<Category>> categories = new ArrayList<>();
        categories.add(null);
        for (long i = 1; i <= CATEGORY_COUNT ; i++) {
            Optional<Category> category = Optional.of(new Category(i, "C" + i));
            categories.add(category);
        }
        Mockito.when(categoryDao.findById(1L)).thenReturn(categories.get(1));
        Mockito.when(categoryDao.findById(2L)).thenReturn(categories.get(2));
        Mockito.when(categoryDao.findById(3L)).thenReturn(categories.get(3));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        List<Category> actual = service.getCategories();
        assertEquals(List.of(categories.get(1).get(), categories.get(2).get(), categories.get(3).get()), actual);
    }

    @Test
    void shouldReturnCategoryListSaveOld() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("\r".getBytes()));
        List<Optional<Category>> categories = new ArrayList<>();
        categories.add(null);
        for (long i = 1; i <= CATEGORY_COUNT ; i++) {
            Optional<Category> category = Optional.of(new Category(i, "C" + i));
            categories.add(category);
        }
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        List<Category> actual = service.getCategories(List.of(categories.get(1).get(), categories.get(2).get(), categories.get(3).get()));
        assertEquals(List.of(categories.get(1).get(), categories.get(2).get(), categories.get(3).get()), actual);
    }

    @Test
    void shouldReturnEmptyCategoryListOnNullInput() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("null".getBytes()));
        List<Optional<Category>> categories = new ArrayList<>();
        categories.add(null);
        for (long i = 1; i <= CATEGORY_COUNT ; i++) {
            Optional<Category> category = Optional.of(new Category(i, "C" + i));
            categories.add(category);
        }
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        List<Category> actual = service.getCategories(List.of(categories.get(1).get(), categories.get(2).get(), categories.get(3).get()));
        assertEquals(new ArrayList<>(), actual);
    }

    @Test
    void shouldReturnCategoryListWhenInvokeWithOldList() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        printService = new PrintServiceWithStream(new PrintStream(buffer), new ByteArrayInputStream("1,2,3".getBytes()));
        List<Optional<Category>> categories = new ArrayList<>();
        categories.add(null);
        for (long i = 1; i <= CATEGORY_COUNT ; i++) {
            Optional<Category> category = Optional.of(new Category(i, "C" + i));
            categories.add(category);
        }
        Mockito.when(categoryDao.findById(1L)).thenReturn(categories.get(1));
        Mockito.when(categoryDao.findById(2L)).thenReturn(categories.get(2));
        Mockito.when(categoryDao.findById(3L)).thenReturn(categories.get(3));
        UserInterfaceService service = new UserInterfaceServiceImpl(printService, authorDao, genreDao, categoryDao, bookDao);
        List<Category> actual = service.getCategories(List.of(categories.get(1).get()));
        assertEquals(List.of(categories.get(1).get(), categories.get(2).get(), categories.get(3).get()), actual);
    }
}