package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.embedded.PostStatistics;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostByUser {

    private String id;

    private String title;

    private Boolean isActive;

    private PostStatistics postStatistics;

    private LocalDateTime publicationTime;

    private String postCardImageUrl;
}
