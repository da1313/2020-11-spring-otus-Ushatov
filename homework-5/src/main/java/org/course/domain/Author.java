package org.course.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private Long id;
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
