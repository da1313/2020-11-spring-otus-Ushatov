package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@Document
public class Genre {

    @Id
    private String id;

    private String name;

    public static Genre of(String name){
        return new Genre(null, name);
    }

}
