package org.course.listeners;

import org.assertj.core.api.Assertions;
import org.course.changelog.InitTestData;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.repository.GenreRepository;
import org.course.testconfig.EmbeddedMongoConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;


@DisplayName("Class GenreMongoEventListener")
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import({EmbeddedMongoConfig.class, InitTestData.class, GenreMongoEventListener.class})
class GenreMongoEventListenerTest {

    public static final String NEW_GENRE_NAME = "NEW_GENRE_NAME";
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldUpdateGenreInBookCollection() {

        Genre genre = mongoOperations.findAll(Genre.class).get(0);

        List<String> bookWithGenreIds = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getGenres().contains(genre)).map(Book::getId).collect(Collectors.toList());

        genre.setName(NEW_GENRE_NAME);

        genreRepository.save(genre);

        bookWithGenreIds.forEach(i -> Assertions.assertThat(mongoOperations.findById(i, Book.class))
                .extracting(Book::getGenres).matches(genres -> genres.contains(genre)));

    }

    @Test
    void shouldDeleteGenreInBookCollection() {

        Genre genre = mongoOperations.findAll(Genre.class).get(0);

        List<String> bookWithGenreIds = mongoOperations.findAll(Book.class).stream()
                .filter(b -> b.getGenres().contains(genre)).map(Book::getId).collect(Collectors.toList());

        genreRepository.delete(genre);

        bookWithGenreIds.forEach(i -> Assertions.assertThat(mongoOperations.findById(i, Book.class))
                .extracting(Book::getGenres).matches(genres -> !genres.contains(genre)));

    }

}