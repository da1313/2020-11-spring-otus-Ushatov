package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.course.domain.embedded.BookEmbedded;
import org.course.domain.embedded.UserEmbedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document
public class Score {

    @Id
    private String id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private UserEmbedded user;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private BookEmbedded book;

    private int value;

    public static Score of(User user, Book book, int value){
        return new Score(null, new UserEmbedded(user.getId(), user.getName()),
                new BookEmbedded(book.getId(), book.getTitle()), value);
    }

}
