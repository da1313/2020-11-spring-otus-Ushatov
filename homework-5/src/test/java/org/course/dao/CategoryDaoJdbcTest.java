package org.course.dao;

import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class CategoryDaoJdbc:")
@JdbcTest
@Import({CategoryDaoJdbc.class, BookDaoJdbc.class})
class CategoryDaoJdbcTest {

    public static final int AUTHOR_COUNT = 2;
    public static final int GENRE_COUNT = 3;
    public static final int CATEGORY_COUNT = 3;
    public static final int BOOK_COUNT = 6;
    public static final String NEW_CATEGORY = "NEW_CATEGORY";
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private BookDao bookDao;
    private List<Author> authors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    @BeforeEach
    void objectModelInit(){
        authors.add(null);
        genres.add(null);
        categories.add(null);
        books.add(null);
        for (long i = 1; i <= AUTHOR_COUNT; i++) {
            Author author = new Author();
            author.setId(i);
            author.setName("A" + i);
            authors.add(author);
        }
        for (long i = 1; i <= GENRE_COUNT; i++) {
            Genre genre = new Genre();
            genre.setId(i);
            genre.setName("G" + i);
            genres.add(genre);
        }
        for (long i = 1; i <= CATEGORY_COUNT; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName("C" + i);
            categories.add(category);
        }
        for (long i = 1; i <= BOOK_COUNT; i++) {
            Book book  = new Book();
            book.setId(i);
            book.setName("B" + i);
            books.add(book);
        }
        books.get(1).setAuthor(authors.get(1));
        books.get(2).setAuthor(authors.get(1));
        books.get(3).setAuthor(authors.get(1));
        books.get(4).setAuthor(authors.get(2));
        books.get(5).setAuthor(authors.get(2));
        books.get(6).setAuthor(authors.get(2));
        books.get(1).setGenre(genres.get(1));
        books.get(2).setGenre(genres.get(1));
        books.get(3).setGenre(genres.get(2));
        books.get(4).setGenre(genres.get(3));
        books.get(5).setGenre(genres.get(3));
        books.get(6).setGenre(genres.get(1));
        books.get(1).getCategories().addAll(List.of(categories.get(1), categories.get(2), categories.get(3)));
        books.get(2).getCategories().addAll(List.of(categories.get(1), categories.get(2)));
        books.get(3).getCategories().addAll(List.of(categories.get(3)));
        categories.get(1).getBooks().addAll(List.of(books.get(1), books.get(2)));
        categories.get(2).getBooks().addAll(List.of(books.get(1), books.get(2)));
        categories.get(3).getBooks().addAll(List.of(books.get(3)));
    }

    @Test
    void shouldFindEntityBySpecifiedId() {
        Category expected = categories.get(1);
        Category actual = categoryDao.findById(1).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllEntities() {
        List<Category> expected = List.of(categories.get(1), categories.get(2), categories.get(3));
        List<Category> actual = categoryDao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateNewEntity(){
        Category expected = new Category();
        expected.setName(NEW_CATEGORY);
        Long actualId = categoryDao.createAndIncrement(expected);
        Category actual = categoryDao.findById(actualId).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldAddBookToCategory() {
        Category category = categoryDao.findById(3).get();
        Book book = bookDao.findById(2).get();
        categoryDao.addBook(category, book);
        Category category1 = categoryDao.findById(3).get();
        assertTrue(category1.getBooks().contains(book));
    }

    @Test
    void shouldRemoveBookFromCategory(){
        Category category = categoryDao.findById(2).get();
        Book book = bookDao.findById(2).get();
        categoryDao.removeBook(category, book);
        Category category1 = categoryDao.findById(2).get();
        assertFalse(category1.getBooks().contains(book));
    }

    @Test
    void shouldUpdateExistingEntity() {
        Category expected = categoryDao.findById(1).get();
        expected.setName(NEW_CATEGORY);
        categoryDao.update(expected);
        Category actual = categoryDao.findById(1).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteEntityWithSpecifiedId() {
        categoryDao.deleteById(1);
        Optional<Category> actual = categoryDao.findById(1);
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldDeleteAllEntities() {
        categoryDao.delete();
        List<Category> actual = categoryDao.findAll();
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldReturnEntityCount(){
        long count = categoryDao.count();
        assertEquals(CATEGORY_COUNT, count);
    }
}