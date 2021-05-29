package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.embedded.UserShort;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

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

    private Boolean isCurrentUser;

}