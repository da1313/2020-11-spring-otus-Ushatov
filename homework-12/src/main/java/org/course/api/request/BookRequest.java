package org.course.api.request;

import lombok.Data;

import java.util.List;

@Data
public class BookRequest {

    private final String title;

    private final List<Long> genreIds;

    private final long authorId;

    private final String description;

}
