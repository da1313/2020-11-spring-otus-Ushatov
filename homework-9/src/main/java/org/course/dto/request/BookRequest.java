package org.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    private String title;

    private List<Long> genreIds;

    private long AuthorId;

    private String description;

}
