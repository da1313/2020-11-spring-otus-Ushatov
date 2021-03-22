package org.course.api.responces;

import lombok.Data;
import org.course.api.pojo.BookShort;

import java.util.List;

@Data
public class BookListResponse {

    private final List<BookShort> bookList;

    private final long totalPages;

}
