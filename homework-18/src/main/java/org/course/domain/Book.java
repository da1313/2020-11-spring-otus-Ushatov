package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.course.domain.embedded.Info;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Document
public class Book {

    @Id
    private String id;

    private String title;

    private LocalDateTime time;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Author author;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Genre> genres;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Info info;

    public static Book of(String title, Author author, List<Genre> genres){
        return new Book(null, title, LocalDateTime.now(), author, genres, new Info());
    }

    public static Book ofEmpty(){
        return new Book(null, null, null, null, null, null);
    }

}
