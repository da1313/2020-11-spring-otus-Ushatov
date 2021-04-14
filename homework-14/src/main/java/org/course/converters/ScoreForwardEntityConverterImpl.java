package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.ScoreNosql;
import org.course.domain.nosql.embedded.BookEmbedded;
import org.course.domain.nosql.embedded.UserEmbedded;
import org.course.domain.sql.Score;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScoreForwardEntityConverterImpl implements EntityConverter<Score, ScoreNosql> {

    private final KeyHolder<Long, String> keyHolder;

    @Override
    public ScoreNosql convert(Score input) {
        String scoreId = keyHolder.getNewKey(EntityName.SCORE);
        String userId = keyHolder.getKey(EntityName.USER, input.getUser().getId());
        String bookId = keyHolder.getKey(EntityName.BOOK, input.getBook().getId());
        UserEmbedded user = new UserEmbedded(userId, input.getUser().getName());
        BookEmbedded book = new BookEmbedded(bookId, input.getBook().getTitle());
        return new ScoreNosql(scoreId, user, book, input.getScore());
    }
}
