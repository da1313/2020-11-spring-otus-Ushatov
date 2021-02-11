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

        bookRepository.increaseCommentCountById(comment.getBook().getId());

    }

}
