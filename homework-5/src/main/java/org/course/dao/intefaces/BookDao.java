package org.course.dao.intefaces;

import org.course.domain.Book;
import org.course.domain.Category;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    Optional<Book> findById(long id);

    List<Book> findAll();

    Long createAndIncrement(Book book);

    void update(Book book);

    void addCategory(Book book, Category category);

    void removeCategory(Book book, Category category);

    void deleteById(long id);

    void delete();

    long count();
}
