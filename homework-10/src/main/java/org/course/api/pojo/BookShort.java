package org.course.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.domain.Author;
import org.course.domain.Genre;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookShort {

    private String id;

    private String title;

    private LocalDateTime time;

    private Author author;

    private List<Genre> genres;

    private int commentsCount;

    private double avgScore;
}
