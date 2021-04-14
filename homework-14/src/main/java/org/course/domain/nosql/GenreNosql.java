package org.course.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@Document("genre")
public class GenreNosql {

    @Id
    private String id;

    private String name;

    public static GenreNosql of(String name){
        return new GenreNosql(null, name);
    }

}
