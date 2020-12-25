package org.course.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long id;
    private String name;
    private Author author;
    private Genre genre;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Category> categories = new ArrayList<>();

    public Book(String name, Author author, Genre genre) {
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

    public Book(String name, Author author, Genre genre, List<Category> categories) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.categories = categories;
    }

    public Book(Long id, String name, Author author, Genre genre) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }
}
