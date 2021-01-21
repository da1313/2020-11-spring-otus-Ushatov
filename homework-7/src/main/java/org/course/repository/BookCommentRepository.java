package org.course.repository;

import org.course.domain.Book;
import org.course.domain.BookComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.LOAD)
    List<BookComment> findByBook(Book book, Pageable pageable);

    @EntityGraph(attributePaths = {"book", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<BookComment> findWithBookAndUserById(long id);

    @EntityGraph(attributePaths = {"book.author", "user"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select bc from BookComment bc")
    List<BookComment> findAllWithBookAndUser(Pageable pageable);

}
