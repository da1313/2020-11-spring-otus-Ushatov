package org.course.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document
public class Author {

    @Id
    private String id;

    private String name;

    public static Author of(String name){
        return new Author(null, name);
    }

    public static Author ofEmpty(){
        return new Author(null, null);
    }
}
