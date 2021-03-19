package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@DisplayName("Class CommentRepository")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    public static final int OVERFLOW_TEST_DATA_SIZE = 100;
    public static final int EXPECTED_QUERY_COUNT = 1;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldFindCommentsByBook() {

        Book book = testEntityManager.find(Book.class, 1L);
        List<Comment> comments = book.getComments();

        comments.forEach(c -> System.out.println(c.getBook() + "*" + c.getUser()));

        testEntityManager.clear();

        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();

        List<Comment> actual = commentRepository.findByBook(book, PageRequest.of(0, OVERFLOW_TEST_DATA_SIZE)).toList();

        Assertions.assertThat(actual).containsExactlyElementsOf(comments);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }
}