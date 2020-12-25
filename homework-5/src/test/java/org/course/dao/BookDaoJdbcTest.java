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

@DisplayName("Class BookDaoJdbc:")
@JdbcTest
@Import({BookDaoJdbc.class, CategoryDaoJdbc.class})
class BookDaoJdbcTest {

    public static final int AUTHOR_COUNT = 2;
    public static final int GENRE_COUNT = 3;
    public static final int CATEGORY_COUNT = 3;
    public static final int BOOK_COUNT = 6;
    public static final String NEW_BOOK_NAME = "NEW";
    private List<Author> authors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    @Autowired
    private BookDao bookDao;
    @Autowired
    private CategoryDao categoryDao;

    @BeforeEach
    private void objectModelInit(){
        authors.add(null);
        genres.add(null);
        categories.add(null);
        books.add(null);
        for (long i = 1; i <= AUTHOR_COUNT; i++) {
            Author author = new Author(i, "A" + i);
            authors.add(author);
        }
        for (long i = 1; i <= GENRE_COUNT; i++) {
            Genre genre = new Genre(i, "G" + i);
            genres.add(genre);
        }
        for (long i = 1; i <= CATEGORY_COUNT; i++) {
            Category category = new Category(i, "C" + i);
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
    void shouldFindEntityById() {
        Book expected = books.get(1);
        Book actual = bookDao.findById(1).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindAllEntities() {
        List<Book> expected = new ArrayList<>();
        for (int i = 1; i <= BOOK_COUNT; i++) {
            expected.add(books.get(i));
        }
        List<Book> actual = bookDao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateBookWithEmptyAuthorAndGenre() {
        Book expected = new Book();
        expected.setName(NEW_BOOK_NAME);
        Long actualId = bookDao.createAndIncrement(expected);
        Book actual = bookDao.findById(actualId).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateBookWithEmptyAuthor() {
        Book expected = new Book();
        expected.setName(NEW_BOOK_NAME);
        expected.setGenre(genres.get(1));
        Long actualId = bookDao.createAndIncrement(expected);
        Book actual = bookDao.findById(actualId).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateBookWithEmptyGenre() {
        Book expected = new Book();
        expected.setName(NEW_BOOK_NAME);
        expected.setAuthor(authors.get(1));
        Long actualId = bookDao.createAndIncrement(expected);
        Book actual = bookDao.findById(actualId).get();
        assertEquals(expected, actual);
    }

    @Test
    void shouldCreateBookWithAuthorAndGenre() {
        Book expected = new Book(NEW_BOOK_NAME, authors.get(1), genres.get(1));
        Long actualId = bookDao.createAndIncrement(expected);
        Book actual = bookDao.findById(actualId).get();
        assertEquals(expected, actual);
    }



    @Test
    void shouldSetBookNameAuthorGenre() {
        Book book = bookDao.findById(1).get();
        book.setName(NEW_BOOK_NAME);
        book.setAuthor(authors.get(2));
        book.setGenre(genres.get(2));
        bookDao.update(book);
        Book actual = bookDao.findById(1).get();
        assertEquals(book, actual);
    }

    @Test
    void shouldSetBookName() {
        Book book = bookDao.findById(1).get();
        book.setName(NEW_BOOK_NAME);
        bookDao.update(book);
        Book actual = bookDao.findById(1).get();
        assertEquals(book, actual);
    }

    @Test
    void shouldSetAuthor() {
        Book book = bookDao.findById(1).get();
        book.setAuthor(authors.get(2));
        bookDao.update(book);
        Book actual = bookDao.findById(1).get();
        assertEquals(book, actual);
    }

    @Test
    void shouldSetGenre() {
        Book book = bookDao.findById(1).get();
        book.setGenre(genres.get(2));
        bookDao.update(book);
        Book actual = bookDao.findById(1).get();
        assertEquals(book, actual);
    }

    @Test
    void shouldSetAuthorGenreToNull() {
        Book book = bookDao.findById(1).get();
        book.setAuthor(null);
        book.setGenre(null);
        bookDao.update(book);
        Book actual = bookDao.findById(1).get();
        assertEquals(book, actual);
    }

    @Test
    void shouldSetAuthorToNull() {
        Book book = bookDao.findById(1).get();
        book.setAuthor(null);
        book.setGenre(genres.get(2));
        bookDao.update(book);
        Book actual = bookDao.findById(1).get();
        assertEquals(book, actual);
    }

    @Test
    void shouldSetGenreToNull() {
        Book book = bookDao.findById(1).get();
        book.setAuthor(authors.get(2));
        book.setGenre(null);
        bookDao.update(book);
        Book actual = bookDao.findById(1).get();
        assertEquals(book, actual);
    }

    @Test
    void shouldAddCategoryToBook(){
        Book book = bookDao.findById(3).get();
        Category category = categoryDao.findById(3).get();
        bookDao.addCategory(book, category);
        Book book1 = bookDao.findById(3).get();
        assertAll(
                () -> assertTrue(book.getCategories().contains(category)),
                () -> assertTrue(book1.getCategories().contains(category))
        );
    }

    @Test
    void shouldRemoveCategoryFromBook(){
        Book book = bookDao.findById(1).get();
        Category category = categoryDao.findById(3).get();
        bookDao.removeCategory(book, category);
        Book book1 = bookDao.findById(1).get();
        assertAll(
                () -> assertFalse(book.getCategories().contains(category)),
                () -> assertFalse(book1.getCategories().contains(category))
        );
    }

    @Test
    void shouldDeleteEntityWithSpecifiedId() {
        bookDao.deleteById(1);
        Optional<Book> actual = bookDao.findById(1);
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldDeleteAllEntities() {
        bookDao.delete();
        List<Book> actual = bookDao.findAll();
    }

    @Test
    void shouldReturnEntityCount(){
        long count = bookDao.count();
        assertEquals(BOOK_COUNT, count);
    }
}