package org.course.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.course.domain.nosql.embedded.BookEmbedded;
import org.course.domain.nosql.embedded.UserEmbedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("score")
public class ScoreNosql {

    @Id
    private String id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEmbedded user;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private BookEmbedded book;

    private int value;

    public static ScoreNosql of(UserNosql user, BookNosql book, int value){
        return new ScoreNosql(null, new UserEmbedded(user.getId(), user.getName()),
                new BookEmbedded(book.getId(), book.getTitle()), value);
    }

}
