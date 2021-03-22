package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.course.domain.embedded.BookEmbedded;
import org.course.domain.embedded.UserEmbedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document
public class Comment {

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

    public static Comment of(String text, User user, Book book){
        return new Comment(null, text, LocalDateTime.now(), new UserEmbedded(user.getId(), user.getName()),
                new BookEmbedded(book.getId(), book.getTitle()));
    }

}
