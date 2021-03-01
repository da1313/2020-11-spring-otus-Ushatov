package org.course.components.interfaces;

import org.course.domain.Book;
import org.course.domain.Genre;
import org.springframework.data.domain.Page;

public interface BookByGenreRepoResolver {

    Page<Book> getPage(Genre genre, int pageNumber);

}
