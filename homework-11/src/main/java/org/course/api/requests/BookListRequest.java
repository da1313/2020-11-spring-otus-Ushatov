package org.course.api.requests;

import lombok.Data;
import org.course.domain.embedded.BookSort;
import org.springframework.data.domain.Sort;

@Data
public class BookListRequest {

    private final int pageNumber;

    private final int pageSize;

    private final String sort;

}
