package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.CommentNosql;
import org.course.domain.nosql.embedded.BookEmbedded;
import org.course.domain.nosql.embedded.UserEmbedded;
import org.course.domain.sql.Comment;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentForwardEntityConverterImpl implements EntityConverter<Comment, CommentNosql> {

    private final KeyHolder<Long, String> keyHolder;

    @Override
    public CommentNosql convert(Comment input) {
        String commentId = keyHolder.getNewKey(EntityName.COMMENT);
        String userId = keyHolder.getKey(EntityName.USER, input.getUser().getId());
        String bookId = keyHolder.getKey(EntityName.BOOK, input.getBook().getId());
        UserEmbedded user = new UserEmbedded(userId, input.getUser().getName());
        BookEmbedded book = new BookEmbedded(bookId, input.getBook().getTitle());
        keyHolder.saveKey(EntityName.COMMENT, input.getId(), commentId);//todo maybe not needed
        return new CommentNosql(commentId, input.getText(), input.getTime(), user, book);
    }
}
