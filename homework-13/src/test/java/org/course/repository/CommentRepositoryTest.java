package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Class CommentRepository")
@DataJpaTest
class CommentRepositoryTest {

    public static final int OVERFLOW_TEST_DATA_SIZE = 100;
    public static final int EXPECTED_QUERY_COUNT = 1;
    public static final long BOOK_ID = 4;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldFindCommentsByBook() {

        Book book = testEntityManager.find(Book.class, BOOK_ID);

        CriteriaBuilder criteriaBuilder = testEntityManager.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Comment> query = criteriaBuilder.createQuery(Comment.class);
        query.select(query.from(Comment.class));
        List<Comment> commentList = testEntityManager.getEntityManager().createQuery(query).getResultList().stream()
                .filter(c -> c.getBook().equals(book)).collect(Collectors.toList());

        commentList.forEach(c -> System.out.println(c.getBook() + "*" + c.getUser()));

        testEntityManager.clear();

        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();

        List<Comment> actual = commentRepository.findByBook(book, PageRequest.of(0, OVERFLOW_TEST_DATA_SIZE)).toList();

        List<Long> actualIds = actual.stream().map(Comment::getId).collect(Collectors.toList());
        List<String> actualText = actual.stream().map(Comment::getText).collect(Collectors.toList());
        List<String> actualUsernames = actual.stream().map(c -> c.getUser().getUsername()).collect(Collectors.toList());

        Assertions.assertThat(actualIds).isEqualTo(commentList.stream().map(Comment::getId).collect(Collectors.toList()));
        Assertions.assertThat(actualText).isEqualTo(commentList.stream().map(Comment::getText).collect(Collectors.toList()));
        Assertions.assertThat(actualUsernames).isEqualTo(commentList.stream().map(c -> c.getUser().getUsername()).collect(Collectors.toList()));

        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }
}