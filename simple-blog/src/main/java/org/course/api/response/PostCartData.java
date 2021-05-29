package org.course.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.model.embedded.PostStatistics;
import org.course.model.embedded.UserShort;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCartData {

    private String id;

    private String title;

    private UserShort user;

    private PostStatistics postStatistics;

    private LocalDateTime publicationTime;

    private String postCardImageUrl;

}
