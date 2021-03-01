package org.course.repository;

import org.assertj.core.api.Assertions;
import org.course.domain.Genre;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@DisplayName("Class GenreRepository")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreRepositoryTest {

    public static final int EXPECTED_QUERY_COUNT = 1;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldFindGenresWithSpecifiedIds() {

        CriteriaQuery<Genre> query = testEntityManager.getEntityManager().getCriteriaBuilder().createQuery(Genre.class);
        List<Genre> genreList = testEntityManager.getEntityManager().createQuery(query.select(query.from(Genre.class))).getResultList();

        List<Genre> customGenres = List.of(genreList.get(0), genreList.get(2));
        List<Long> customGenresIds = customGenres.stream().map(Genre::getId).collect(Collectors.toList());

        testEntityManager.clear();

        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();

        Set<Genre> actual = genreRepository.findByIds(customGenresIds);

        Assertions.assertThat(actual).containsExactlyElementsOf(customGenres);
        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERY_COUNT);

    }
}