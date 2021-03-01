package org.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;
import org.course.utility.Route;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainPageRequest {

    private MainPageBehavior action;

    private Route direction;

    private Long genreId;

    private BookSort sort;

    private String query;

}
