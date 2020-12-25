package org.course.dao.mappers;

import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        long bookId = resultSet.getLong("bookId");
        String bookName = resultSet.getString("bookName");
        long authorId = resultSet.getLong("authorId");
        Author author = authorId == 0 ? null : new Author(authorId, resultSet.getString("authorName"));
        long genreId = resultSet.getLong("genreId");
        Genre genre = genreId == 0 ? null : new Genre(genreId, resultSet.getString("genreName"));
        return new Book(bookId, bookName, author, genre);
    }
}
