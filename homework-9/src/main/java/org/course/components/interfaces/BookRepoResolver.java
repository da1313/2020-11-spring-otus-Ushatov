package org.course.components.interfaces;

import org.course.domain.Book;
import org.course.utility.BookSort;
import org.springframework.data.domain.Page;

public interface BookRepoResolver {

    Page<Book> getPage(int pageNumber);

    BookSort getSort();

}
