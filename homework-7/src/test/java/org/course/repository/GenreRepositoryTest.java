package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.domain.Genre;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@DisplayName("Class GenreRepository")
@DataJpaTest
class GenreRepositoryTest {

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 3;
    private static final long EXPECTED_QUERY_COUNT = 1L;
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllWithBook() {
        List<Genre> expected = entityManager.getEntityManager()
                .createQuery("select g from Genre g", Genre.class)
                .setFirstResult(PAGE_NUMBER * PAGE_SIZE)
                .setMaxResults(PAGE_SIZE)
                .getResultList();
        expected.forEach(g -> System.out.println(g.getBooks()));
        entityManager.clear();
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();
        List<Genre> actual = genreRepository.findAllWithBook(PageRequest.of(PAGE_NUMBER, PAGE_SIZE));
        Assertions.assertThat(actual).hasSize(expected.size()).isEqualTo(expected);
        actual.forEach(a -> Assertions.assertThat(a.getBooks())
                .containsExactlyElementsOf(expected.stream().filter(e -> e.getId() == a.getId()).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Genre with id " + a.getId() + " not found!"))
                        .getBooks()));
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);
    }
}