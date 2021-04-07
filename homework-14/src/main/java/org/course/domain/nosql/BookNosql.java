package org.course.domain.nosql;

import lombok.*;
import org.course.domain.nosql.embedded.Info;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document("book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookNosql {

    @Id
    private String id;

    private String title;

    private LocalDateTime time;

    private String description;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AuthorNosql author;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<GenreNosql> genres;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Info info;

    public static BookNosql of(String title, String description, AuthorNosql author, List<GenreNosql> genres){
        return new BookNosql(null, title, LocalDateTime.now(), description,  author, genres, new Info());
    }

    public static BookNosql ofEmpty(){
        return new BookNosql(null, null, null, null, null, null, null);
    }

}
