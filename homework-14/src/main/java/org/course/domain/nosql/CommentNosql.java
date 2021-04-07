package org.course.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.course.domain.nosql.embedded.BookEmbedded;
import org.course.domain.nosql.embedded.UserEmbedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document("comment")
public class CommentNosql {

    @Id
    private String id;

    private String text;

    private LocalDateTime time;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEmbedded user;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private BookEmbedded book;

    public static CommentNosql of(String text, UserNosql user, BookNosql book){
        return new CommentNosql(null, text, LocalDateTime.now(), new UserEmbedded(user.getId(), user.getName()),
                new BookEmbedded(book.getId(), book.getTitle()));
    }

}
