package org.course.dto.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainPageParams {

    private MainPageBehavior action;

    private Long genreId;

    private BookSort sort;

    private int currentPage;

    private int totalPages;

    private String query;

}
