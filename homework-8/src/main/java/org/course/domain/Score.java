package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document
public class Score {

    @Id
    private String id;

    @DBRef
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @DBRef
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Book book;

    private int value;

    public static Score of(User user, Book book, int value){
        return new Score(null, user, book, value);
    }

}
