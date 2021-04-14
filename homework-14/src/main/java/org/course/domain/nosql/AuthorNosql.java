package org.course.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("author")
public class AuthorNosql {

    @Id
    private String id;

    private String name;

    public static AuthorNosql of(String name){
        return new AuthorNosql(null, name);
    }

    public static AuthorNosql ofEmpty(){
        return new AuthorNosql(null, null);
    }
}
