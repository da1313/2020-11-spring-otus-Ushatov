package org.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.embedded.PostStatistics;
import org.course.model.embedded.UserShort;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    private String id;

    private String title;

    private String text;

    private LocalDateTime dateTime;

    private UserShort user;

    private PostStatistics postStatistics;

    private String moderationStatus;

    private LocalDateTime publicationTime;

    private Boolean isActive;

    private String postCardImageUrl;

//    private String themeId;//todo add

}
