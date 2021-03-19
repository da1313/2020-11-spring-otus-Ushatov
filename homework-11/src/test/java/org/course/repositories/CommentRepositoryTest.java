package org.course.repositories;

import org.assertj.core.api.Assertions;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import testdata.TestDataLoader;

import java.util.List;

@DisplayName("Class CommentRepository")
@DataMongoTest
@Import(TestDataLoader.class)
class CommentRepositoryTest {

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldFindCountByBook() {

        Book book = bookRepository.findAll().blockFirst();

        List<Comment> commentList = mongoOperations.findAll(Comment.class).collectList().block();

        long expected = commentList.stream().filter(c -> c.getBook().getId().equals(book.getId())).count();

        long actual = commentRepository.findCountByBook(book.getId()).block().getValue();

        Assertions.assertThat(actual).isEqualTo(expected);

    }
}