package org.course.repository;

import org.course.domain.Genre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @EntityGraph(attributePaths = "books", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select g from Genre g")
    List<Genre> findAllWithBook(Pageable pageable);

}
