package org.course.dto.state;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainPageParams {

    private long genreId;

    private String sort;

    private int currentPage;

    private int totalPages;

    private boolean isSearch;

    private String query;

}
