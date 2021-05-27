package org.course.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    private String title;

    private String text;

    private LocalDateTime publicationTime;

    private Boolean isActive;

    private String postCardImageUrl;

}
