package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.repository.BookRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommentMongoEventListener extends AbstractMongoEventListener<Comment> {

    private final BookRepository bookRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Comment> event) {

        Comment comment = event.getSource();

        Book book = bookRepository.findById(comment.getBook().getId())
                .orElseThrow(() -> new IllegalArgumentException("Book whit id " + comment.getBook().getId() + " not found!"));

        book.getInfo().setCount(book.getInfo().getCount() + 1);

        book.getInfo().calculate();

        bookRepository.save(book);

    }

}
