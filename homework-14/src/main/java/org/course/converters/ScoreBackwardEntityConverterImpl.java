package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.ScoreNosql;
import org.course.domain.sql.Book;
import org.course.domain.sql.Comment;
import org.course.domain.sql.Score;
import org.course.domain.sql.User;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScoreBackwardEntityConverterImpl implements EntityConverter<ScoreNosql, Score> {

    private final KeyHolder<String, Long> keyHolder;

    @Override
    public Score convert(ScoreNosql input) {
        Long scoreId = keyHolder.getNewKey(EntityName.SCORE);
        Long bookId = keyHolder.getKey(EntityName.BOOK, input.getBook().getId());
        Long userId = keyHolder.getKey(EntityName.USER, input.getUser().getId());
        Book book = new Book(bookId, null, null, null, null, null, null);
        User user = new User(userId, null, null, true);
        return new Score(scoreId, input.getValue(), user, book);
    }
}
