package org.course.repository;

import org.course.domain.Book;
import org.course.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByBook(Book book, Pageable pageable);

    void deleteByBookId(String bookId);

    void deleteByUserId(String userId);
}
