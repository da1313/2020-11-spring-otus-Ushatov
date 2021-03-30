package org.course.service.interfaces;

import org.course.api.attributes.BookPageAttributes;
import org.course.api.attributes.BooksPageAttributes;
import org.course.api.attributes.UpdateBookPageAttributes;

public interface PagesService {

    BooksPageAttributes getBooksPageAttributes(int pageNumber);

    UpdateBookPageAttributes getUpdateBookPageAttributes(long id);

    UpdateBookPageAttributes getCreateBookPageAttributes();

    BookPageAttributes getBookPageAttributes(long id, int page);
}
