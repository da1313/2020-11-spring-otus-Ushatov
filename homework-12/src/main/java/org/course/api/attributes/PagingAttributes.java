package org.course.api.attributes;

import lombok.Data;

import java.util.List;

@Data
public class PagingAttributes {

    private final int lastPage;
    private final List<Integer> pagesBefore;
    private final List<Integer> pagesAfter;
    private final int pageNumber;
    private final int nextPage;
    private final int previousPage;

}
