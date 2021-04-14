package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.CommentNosql;
import org.course.domain.sql.Book;
import org.course.domain.sql.Comment;
import org.course.domain.sql.User;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentBackwardEntityConverterImpl implements EntityConverter<CommentNosql, Comment> {

    private final KeyHolder<String, Long> keyHolder;

    @Override
    public Comment convert(CommentNosql input) {
        Long commentId = keyHolder.getNewKey(EntityName.COMMENT);
        Long bookId = keyHolder.getKey(EntityName.BOOK, input.getBook().getId());
        Long userId = keyHolder.getKey(EntityName.USER, input.getUser().getId());
        Book book = new Book(bookId, null, null, null, null, null, null);
        User user = new User(userId, null, null, true);
        keyHolder.saveKey(EntityName.COMMENT, input.getId(), commentId);//todo maybe not needed
        return new Comment(commentId, input.getText(), input.getTime(), book, user);
    }
}
