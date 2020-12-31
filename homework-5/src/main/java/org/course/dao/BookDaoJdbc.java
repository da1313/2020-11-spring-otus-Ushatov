package org.course.dao;

import lombok.AllArgsConstructor;
import org.course.dao.intefaces.BookDao;
import org.course.dao.mappers.BookMapper;
import org.course.dao.mappers.CategoryMapper;
import org.course.domain.Book;
import org.course.domain.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@AllArgsConstructor
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    static class CategoriesOfBook implements ResultSetExtractor<Map<Long, List<Category>>>{
        @Override
        public Map<Long, List<Category>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Long, List<Category>> categoriesOfBook = new HashMap<>();
            while (resultSet.next()){
                long bookId = resultSet.getLong("bookId");
                long categoryId = resultSet.getLong("categoryId");
                String categoryName = resultSet.getString("categoryName");
                if (categoriesOfBook.containsKey(bookId)){
                    categoriesOfBook.get(bookId).add(new Category(categoryId, categoryName));
                } else {
                    List<Category> categories = new ArrayList<>();
                    categories.add(new Category(categoryId, categoryName));
                    categoriesOfBook.put(bookId, categories);
                }
            }
            return categoriesOfBook;
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
                    "select distinct c.id as categoryId," +
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
        Map<Long, List<Category>> categoriesOfBooks = namedParameterJdbcOperations.query(
                "select distinct" +
                    " b.id as bookId," +
                    " c.id as categoryId," +
                    " c.name as categoryName" +
                    " from categories c" +
                    " join books_to_categories btc on c.id = btc.category_id" +
                    " join books b on b.id = btc.book_id", new CategoriesOfBook());
        if (categoriesOfBooks == null) return books;
        books.stream().filter(book -> categoriesOfBooks.containsKey(book.getId()))
                .forEach(book -> book.setCategories(categoriesOfBooks.get(book.getId())));
        return books;
    }

    @Override
    public Long create(Book book) {
        KeyHolder keyHolder  = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", book.getName());
        params.addValue("authorId", book.getAuthor() == null ? null : book.getAuthor().getId());
        params.addValue("genreId", book.getGenre() == null? null : book.getGenre().getId());
        namedParameterJdbcOperations.update("insert into books(name, author_id, genre_id) values (:name, :authorId, :genreId)", params, keyHolder);
        book.setId((Long) keyHolder.getKey());
        return (Long) keyHolder.getKey();
    }

    @Override
    public void update(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("name", book.getName());
        params.put("authorId", book.getAuthor() == null ? null : book.getAuthor().getId());
        params.put("genreId", book.getGenre() == null? null : book.getGenre().getId());
        namedParameterJdbcOperations.update("update books set name = :name, author_id = :authorId, genre_id = :genreId where id = :id", params);
    }

    @Override
    public void addCategory(Book book, Category category) {
        Map<String, Long> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("categoryId", category.getId());
        namedParameterJdbcOperations.update("insert into books_to_categories(book_id, category_id) values(:bookId, :categoryId)", params);
        book.getCategories().add(category);
    }

    @Override
    public void removeCategory(Book book, Category category) {
        Map<String, Long> params = new HashMap<>();
        params.put("bookId", book.getId());
        params.put("categoryId", category.getId());
        namedParameterJdbcOperations.update("delete from books_to_categories" +
                " where book_id = :bookId and category_id = :categoryId", params);
        book.getCategories().remove(category);
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
