package org.course.api.attributes;

import lombok.Data;
import org.course.domain.Book;

import java.util.List;

@Data
public class BooksPageAttributes {

    private final List<Book> bookList;

    private final PagingAttributes pagingAttributes;

}
