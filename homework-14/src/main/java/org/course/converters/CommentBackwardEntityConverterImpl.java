package org.course.converters;

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
public class CommentBackwardEntityConverterImpl implements EntityConverter<CommentNosql, Comment> {

    private final static String JOB_NAME = "nosqlToSqlJob";

    private final List<KeyHolder<String, Long>> keyHolderList;

    private final KeyHolder<String, Long> keyHolder;

    public CommentBackwardEntityConverterImpl(@Autowired List<KeyHolder<String, Long>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

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
