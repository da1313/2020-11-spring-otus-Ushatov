package org.course.service.interfaces;

import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.state.BookPageParams;

public interface BookPageService {

    BookPageAttributes getBookPageAttributes(long bookId, int nextPage, BookPageParams previousParams);

}
