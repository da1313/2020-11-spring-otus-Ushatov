package org.course.dao;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.mappers.BookMapper;
import org.course.dao.mappers.CategoryMapper;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;
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
public class CategoryDaoJdbc implements CategoryDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @AllArgsConstructor
    static class BooksOfCategory{
        Long categoryId;
        Book book;
    }

    static class BooksOfCategoryMapper implements RowMapper<BooksOfCategory> {
        @Override
        public BooksOfCategory mapRow(ResultSet resultSet, int i) throws SQLException {
            long categoryId = resultSet.getLong("categoryId");
            long bookId = resultSet.getLong("bookId");
            String bookName = resultSet.getString("bookName");
            long authorId = resultSet.getLong("authorId");
            Author author = authorId == 0 ? null : new Author(authorId, resultSet.getString("authorName"));
            long genreId = resultSet.getLong("genreId");
            Genre genre = genreId == 0 ? null : new Genre(genreId, resultSet.getString("genreName"));
            Book book = new Book(bookId, bookName, author, genre);
            return new BooksOfCategory(categoryId, book);
        }
    }

    @Override
    public Long create(Category category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", category.getName());
        namedParameterJdbcOperations.update("insert into categories(name) values(:name)", params, keyHolder);
        category.setId((Long) keyHolder.getKey());
        return (Long) keyHolder.getKey();
    }

    @Override
    public Optional<Category> findById(long id) {
        try{
            Map<String, Long> params = Collections.singletonMap("id", id);
            Category category = namedParameterJdbcOperations.queryForObject("select c.id as categoryId, c.name as categoryName" +
                    " from categories c where c.id = :id", params, new CategoryMapper());
            List<Book> books = namedParameterJdbcOperations.query(
                    "select distinct b.id as bookId," +
                    " b.name as bookName," +
                    " a.id as authorId," +
                    " a.name as authorName," +
                    " g.id as genreId," +
                    " g.name as genreName" +
                    " from books_to_categories btc" +
                    " join books b on btc.book_id = b.id" +
                    " join authors a on b.author_id = a.id" +
                    " join genres g on b.genre_id = g.id" +
                    " where btc.category_id = :id", params, new BookMapper());
            if (category != null) category.getBooks().addAll(books);
            return Optional.ofNullable(category);
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = namedParameterJdbcOperations.query("select c.id as categoryId, c.name as categoryName" +
                " from categories c", new CategoryMapper());
        List<BooksOfCategory> booksOfCategories = namedParameterJdbcOperations.query(
                "select distinct btc.category_id as categoryId," +
                " b.id as bookId," +
                " b.name as bookName," +
                " a.id as authorId," +
                " a.name as authorName," +
                " g.id as genreId," +
                " g.name as genreName" +
                " from books_to_categories btc" +
                " join books b on btc.book_id = b.id" +
                " join authors a on b.author_id = a.id" +
                " join genres g on b.genre_id = g.id" +
                " where btc.category_id in (select c.id from categories c)", new BooksOfCategoryMapper());
        categories.forEach(c -> c.getBooks().addAll(booksOfCategories.stream()
                .filter(b -> b.categoryId.equals(c.getId())).map(b -> b.book).collect(Collectors.toList())));
        return categories;
    }

    @Override
    public void addBook(Category category, Book book) {
        Map<String, Long> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("categoryId", category.getId());
        namedParameterJdbcOperations.update("insert into books_to_categories(book_id, category_id) values(:bookId, :categoryId)", params);
        category.getBooks().add(book);
        book.getCategories().add(category);
    }

    @Override
    public void removeBook(Category category, Book book) {
        Map<String, Long> params = new HashMap<>();
        params.put("categoryId", category.getId());
        params.put("bookId", book.getId());
        namedParameterJdbcOperations.update("delete from books_to_categories" +
                " where category_id = :categoryId and book_id =:bookId", params);
        category.getBooks().remove(book);
        book.getCategories().remove(category);
    }

    @Override
    public void update(Category category) {
        if (category.getId() == null){
            throw new IllegalArgumentException("Identifier not set. Can't update the entity, consider persist it first!");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", category.getId());
        params.put("name", category.getName());
        namedParameterJdbcOperations.update("update categories set name = :name where id = :id", params);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        namedParameterJdbcOperations.update("delete from categories where id = :id", params);
    }

    @Override
    public void delete() {
        namedParameterJdbcOperations.getJdbcOperations().execute("delete from categories");
    }

    @Override
    public long count() {
        Long result = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(c.id) from categories c", Long.class);
        return result == null ? 0 : result;
    }

}
