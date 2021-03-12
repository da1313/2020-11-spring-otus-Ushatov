package org.course.api.responces;

import lombok.Data;

import java.util.List;

@Data
public class BookResponse {

    private final String id;

    private final String title;

    private final String authorId;

    private final List<String> genresId;

}
