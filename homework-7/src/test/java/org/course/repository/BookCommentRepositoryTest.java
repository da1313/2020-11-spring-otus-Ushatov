package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.domain.BookComment;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@DisplayName("Class BookCommentRepository")
class BookCommentRepositoryTest {

    public static final int PAGE_SIZE = 3;
    public static final int PAGE_NUMBER = 0;
    public static final long EXPECTED_QUERY_COUNT = 1L;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Test
    void shouldReturnAllCommentsWithBooksAndUsers() {
        List<BookComment> expected = entityManager.getEntityManager()
                .createQuery("select bc from BookComment bc", BookComment.class)
                .setFirstResult(PAGE_NUMBER * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();
        expected.forEach(bc -> System.out.println(bc.getBook() + " " + bc.getUser()));
        entityManager.clear();
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<BookComment> actual = bookCommentRepository.findAllWithBookAndUser(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        Assertions.assertThat(actual).hasSize(expected.size()).isEqualTo(expected);
        Assertions.assertThat(actual).extracting(BookComment::getBook)
                .containsExactlyInAnyOrderElementsOf(expected.stream().map(BookComment::getBook).collect(Collectors.toList()));
        Assertions.assertThat(actual).extracting(BookComment::getUser)
                .containsExactlyInAnyOrderElementsOf(expected.stream().map(BookComment::getUser).collect(Collectors.toList()));
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }
}