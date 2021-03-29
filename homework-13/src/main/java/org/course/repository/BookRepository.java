package org.course.repository;

import org.course.domain.Book;
import org.course.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b where b.id = ?1")
    Optional<Book> findByIdEager(long id);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Book b")
    Page<Book> findAllEager(Pageable pageable);

}
