package org.course.repository;

import org.course.domain.Book;
import org.course.domain.Card;
import org.course.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends MongoRepository<Card, String> {

    Optional<Card> findByUserAndBook(User user, Book book);

    List<Card> findByUser(User user);

}
