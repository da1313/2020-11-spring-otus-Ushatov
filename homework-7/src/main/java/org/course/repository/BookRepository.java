package org.course.repository;

import org.course.domain.Book;
import org.course.repository.projections.BookStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Book> findWithAuthorAndGenreById(long id);

    @Query("select b as book, size(b.comments) as commentCount," +
            " (select count(bs) from BookScore bs where bs.score = 1 and bs.book = b) as one," +
            " (select count(bs) from BookScore bs where bs.score = 2 and bs.book = b) as two," +
            " (select count(bs) from BookScore bs where bs.score = 3 and bs.book = b) as three," +
            " (select count(bs) from BookScore bs where bs.score = 4 and bs.book = b) as four," +
            " (select count(bs) from BookScore bs where bs.score = 5 and bs.book = b) as five from Book b where b = :book")
    Optional<BookStatistics> findBookStatistics(@Param("book") Book book);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b")
    List<Book> findAllWithAuthor(Pageable pageable);

}
