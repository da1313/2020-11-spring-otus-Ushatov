package org.course.dto.state;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookPageParams {

    private String sort;

    private int currentPage;

    private int totalPages;

}
