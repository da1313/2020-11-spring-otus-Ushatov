package org.course.repository.sql;

import org.course.domain.sql.Score;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    @EntityGraph(attributePaths = {"user", "book"})
    @Query("select s from Score s")
    List<Score> findAllEager();

}
