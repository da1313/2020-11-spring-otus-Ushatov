package org.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.embedded.UserShort;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    private String id;

    private LocalDateTime dateTime;

    private String text;

    private UserShort user;

    private String postId;

    private String commentId;

    private Boolean isEnabled;

    private int likes;

    private int dislikes;

}
