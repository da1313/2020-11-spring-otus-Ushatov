package org.course.api.attributes;

import lombok.Data;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;

import java.util.List;

@Data
public class UpdateBookPageAttributes {

    private final List<Author> authors;

    private final List<Genre> genres;

    private final Book book;

}
