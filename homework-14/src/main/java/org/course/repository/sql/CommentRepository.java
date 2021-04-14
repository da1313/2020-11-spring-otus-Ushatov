package org.course.repository.sql;

import org.course.domain.sql.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user", "book"})
    @Query("select c from Comment c")
    List<Comment> findAllEager();

}
