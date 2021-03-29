package org.course.repository;

import org.course.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("select g from Genre g where g.id in ?1")
    Set<Genre> findByIds(List<Long> ids);

}
