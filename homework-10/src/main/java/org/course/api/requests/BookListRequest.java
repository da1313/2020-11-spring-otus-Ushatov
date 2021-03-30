package org.course.api.requests;

import lombok.Data;
import org.course.domain.BookSort;

@Data
public class BookListRequest {

    private final int pageNumber;

    private final int pageSize;

    private final BookSort sort;

}
