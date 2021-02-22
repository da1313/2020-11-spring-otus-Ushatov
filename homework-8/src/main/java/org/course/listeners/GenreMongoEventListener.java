package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.Genre;
import org.course.repository.BookRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Genre> event) {

        Genre genre = event.getSource();

        bookRepository.updateGenreInBookCollection(genre);

    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Genre> event) {

        String  genreId = event.getSource().get("_id").toString();

        bookRepository.deleteGenreInBookCollectionById(genreId);

    }
}
