package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.Book;
import org.course.repository.AuthorRepository;
import org.course.repository.CommentRepository;
import org.course.repository.GenreRepository;
import org.course.repository.ScoreRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final ScoreRepository scoreRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {

        Book book = event.getSource();

        if (book.getAuthor() != null){
            authorRepository.save(book.getAuthor());
        }

        if (!book.getGenres().isEmpty()){
            book.getGenres().forEach(genreRepository::save);
        }
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {

        String bookId = event.getSource().get("_id").toString();

        commentRepository.deleteByBookId(bookId);

        scoreRepository.deleteByBookId(bookId);

    }
}
