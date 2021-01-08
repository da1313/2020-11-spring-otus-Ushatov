package org.course.dao.interfaces;

import org.course.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book save(Book book);

    Optional<Book> findById(long id);

    List<Book> findAll();

    void delete(Book book);

    void deleteAll();

    long count();

}
