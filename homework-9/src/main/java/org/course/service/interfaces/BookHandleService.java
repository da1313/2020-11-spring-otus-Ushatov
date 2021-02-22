package org.course.service.interfaces;

import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.attributes.CreateBookPageAttributes;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.attributes.UpdateBookPageAttributes;
import org.course.dto.state.BookPageParams;
import org.course.dto.state.MainPageParams;

import java.util.List;

public interface BookHandleService {

    MainPageAttributes getMainPageAttributes(long genreId, String sort, int nextPage, boolean isSearch, String query, MainPageParams previousParams);

    BookPageAttributes getBookPageAttributes(long bookId, int nextPage, BookPageParams previousParams);

    CreateBookPageAttributes getCreateBookPageAttributes();

    void createBook(String title, List<Long> genreIds, long authorId, String description);

    UpdateBookPageAttributes getUpdateBookAttributes(long id);

    void updateBook(long id, String title, List<Long> genreIds, long authorId, String description);

    void deleteBook(long id);
}
