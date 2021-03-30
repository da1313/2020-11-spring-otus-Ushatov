package org.course.repository;

import org.course.domain.Book;
import org.course.domain.Score;
import org.course.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    Optional<Score> findByUserAndBook(User user, Book book);

}
