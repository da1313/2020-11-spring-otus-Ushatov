package org.course.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("user")
public class UserNosql {

    @Id
    private String id;

    private String name;

    private String password;

    private boolean active;

    public static UserNosql of(String name, String password){
        return new UserNosql(null, name, password, true);
    }
}
