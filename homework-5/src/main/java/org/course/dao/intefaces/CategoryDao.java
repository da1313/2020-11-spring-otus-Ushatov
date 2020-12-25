package org.course.dao.intefaces;

import org.course.domain.Book;
import org.course.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {

    Long createAndIncrement(Category category);

    Optional<Category> findById(long id);

    List<Category> findAll();

    void addBook(Category category, Book book);

    void removeBook(Category category, Book book);

    void update(Category category);

    void deleteById(long id);

    void delete();

    long count();
}
