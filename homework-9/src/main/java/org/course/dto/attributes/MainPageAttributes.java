package org.course.dto.attributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.dto.state.MainPageParams;

import java.util.List;

@Data
@AllArgsConstructor
public class MainPageAttributes {

    private MainPageParams pageParams;
    private List<Book> bookList;
    private List<Genre> genreList;

}
