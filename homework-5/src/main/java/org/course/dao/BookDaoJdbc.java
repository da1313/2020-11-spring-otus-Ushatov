package org.course.dao;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.BookDao;
import org.course.dao.mappers.BookMapper;
import org.course.dao.mappers.CategoryMapper;
import org.course.domain.Book;
import org.course.domain.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @AllArgsConstructor
    static class CategoriesOfBook {
        Long bookId;
        Category category;
    }

    static class CategoriesOfBookMapper implements RowMapper<CategoriesOfBook> {
        @Override
        public CategoriesOfBook mapRow(ResultSet resultSet, int i) throws SQLException {
            long bookId = resultSet.getLong("bookId");
            long categoryId = resultSet.getLong("categoryId");
            String categoryName = resultSet.getString("categoryName");
            Category category = new Category(categoryId, categoryName);
            return new CategoriesOfBook(bookId, category);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        try {
            Book book = namedParameterJdbcOperations.queryForObject(
                    "select" +
                            " b.id as bookId," +
                            " b.name as bookName," +
                            " a.id as authorId," +
                            " a.name as authorName," +
                            " g.id as genreId," +
                            " g.name as genreName" +
                            " from books b" +
                            " left join authors a on b.author_id = a.id" +
                            " left join genres g on b.genre_id = g.id" +
                            " where b.id = :id", params, new BookMapper());
            List<Category> categories = namedParameterJdbcOperations.query(
                    "select c.id as categoryId," +
                            " c.name as categoryName" +
                            " from categories c" +
                            " join books_to_categories btc on c.id = btc.category_id" +
                            " where btc.book_id = :id", params, new CategoryMapper());
            if (book != null) book.getCategories().addAll(categories);
            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = namedParameterJdbcOperations.query(
                "select" +
                    " b.id as bookId," +
                    " b.name as bookName," +
                    " a.id as authorId," +
                    " a.name as authorName," +
                    " g.id as genreId," +
                    " g.name as genreName" +
                    " from books b" +
                    " left join authors a on b.author_id = a.id" +
                    " left join genres g on b.genre_id = g.id", new BookMapper());
        List<CategoriesOfBook> categoriesOfBooks = namedParameterJdbcOperations.query(
                "select" +
                    " btc.book_id as bookId," +
                    " c.id as categoryId," +
                    " c.name as categoryName" +
                    " from categories c" +
                    " join books_to_categories btc on c.id = btc.category_id" +
                    " where btc.book_id in (select b.id from books b)", new CategoriesOfBookMapper());
        books.forEach(b -> b.getCategories().addAll(categoriesOfBooks.stream()
                .filter(c -> c.bookId.equals(b.getId())).map(c -> c.category).collect(Collectors.toList())));
        return books;
    }

    @Override
    public Long createAndIncrement(Book book) {
        KeyHolder keyHolder  = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        if ((book.getAuthor() == null && book.getGenre() == null)
                || ((book.getAuthor() != null && book.getAuthor().getId() == null) && ((book.getGenre() != null && book.getGenre().getId() == null)))){
            params.addValue("name", book.getName());
            namedParameterJdbcOperations.update("insert into books(name) values (:name)", params, keyHolder);
        } else if (book.getAuthor() == null && (book.getGenre() != null && book.getGenre().getId() != null)){
            params.addValue("name", book.getName());
            params.addValue("genreId", book.getGenre().getId());
            namedParameterJdbcOperations.update("insert into books(name, genre_id) values (:name, :genreId)", params, keyHolder);
        } else if (book.getGenre() == null && (book.getAuthor() != null && book.getAuthor().getId() != null)){
            params.addValue("name", book.getName());
            params.addValue("authorId", book.getAuthor().getId());
            namedParameterJdbcOperations.update("insert into books(name, author_id) values (:name, :authorId)", params, keyHolder);
        } else {
            params.addValue("name", book.getName());
            params.addValue("authorId", book.getAuthor().getId());
            params.addValue("genreId", book.getGenre().getId());
            namedParameterJdbcOperations.update("insert into books(name, author_id, genre_id) values (:name, :authorId, :genreId)", params, keyHolder);
        }
        book.setId((Long) keyHolder.getKey());
        return (Long) keyHolder.getKey();
    }

    @Override
    public void update(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        if ((book.getAuthor() == null && book.getGenre() == null)
                || ((book.getAuthor() != null && book.getAuthor().getId() == null) && ((book.getGenre() != null && book.getGenre().getId() == null)))){
            params.put("name", book.getName());
            namedParameterJdbcOperations.update("update books set name = :name, author_id = null, genre_id = null where id = :id", params);
        } else if (book.getAuthor() == null && (book.getGenre() != null && book.getGenre().getId() != null)){
            params.put("name", book.getName());
            params.put("genreId", book.getGenre().getId());
            namedParameterJdbcOperations.update("update books set name = :name, author_id = null, genre_id = :genreId where id = :id", params);
        } else if (book.getGenre() == null && (book.getAuthor() != null && book.getAuthor().getId() != null)){
            params.put("name", book.getName());
            params.put("authorId", book.getAuthor().getId());
            namedParameterJdbcOperations.update("update books set name = :name, author_id = :authorId, genre_id = null where id = :id", params);
        } else {
            params.put("name", book.getName());
            params.put("authorId", book.getAuthor().getId());
            params.put("genreId", book.getGenre().getId());
            namedParameterJdbcOperations.update("update books set name = :name, author_id = :authorId, genre_id = :genreId where id = :id", params);
        }
    }

    @Override
    public void addCategory(Book book, Category category) {
        Map<String, Long> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("categoryId", category.getId());
        namedParameterJdbcOperations.update("insert into books_to_categories(book_id, category_id) values(:bookId, :categoryId)", params);
        book.getCategories().add(category);
        category.getBooks().add(book);
    }

    @Override
    public void removeCategory(Book book, Category category) {
        Map<String, Long> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("categoryId", category.getId());
        namedParameterJdbcOperations.update("delete from books_to_categories" +
                " where book_id = :bookId and category_id = :categoryId", params);
        book.getCategories().remove(category);
        category.getBooks().remove(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update("delete from books where id = :id", params);
    }

    @Override
    public void delete() {
        namedParameterJdbcOperations.getJdbcOperations().execute("delete from books");
    }

    @Override
    public long count() {
        Long result = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(b.id) from books b", Long.class);
        return result == null ? 0 : result;
    }

}
