package org.course.converters;

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
public class CommentForwardEntityConverterImpl implements EntityConverter<Comment, CommentNosql> {

    private static final String JOB_NAME = "sqlToNosqlJob";

    private final List<KeyHolder<Long, String>> keyHolderList;

    private final KeyHolder<Long, String> keyHolder;

    public CommentForwardEntityConverterImpl(@Autowired List<KeyHolder<Long, String>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

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
