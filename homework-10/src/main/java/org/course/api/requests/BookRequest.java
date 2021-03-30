package org.course.api.requests;

import lombok.Data;

import java.util.List;

@Data
public class BookRequest {

    private final String title;

    private final String authorId;

    private final List<String> genresId;

}
