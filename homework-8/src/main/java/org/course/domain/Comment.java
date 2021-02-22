package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    @DBRef
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @DBRef
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Book book;

    public static Comment of(String text, User user, Book book){
        return new Comment(null, text, LocalDateTime.now(), user, book);
    }

}
