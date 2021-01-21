package org.course.dao.interfaces;

import org.course.domain.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentDao {

    BookComment save(BookComment bookComment);

    Optional<BookComment> findById(long id);

    List<BookComment> findAll();

    void delete(BookComment bookComment);

    void deleteAll();

    long count();

}
