package org.course.dto.attributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateBookPageAttributes {

    private List<Genre> genreList;
    private List<Author> authorList;
    private Book book;

}
