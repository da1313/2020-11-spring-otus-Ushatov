package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.Author;
import org.course.repository.BookRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Author> event) {

        String authorId = event.getSource().get("_id").toString();

        bookRepository.deleteAuthorInBookCollection(authorId);

    }

    @Override
    public void onAfterSave(AfterSaveEvent<Author> event) {

        Author author = event.getSource();

        bookRepository.updateAuthorInBookCollection(author);

    }
}
