package org.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Id
    private String id;

    private Boolean value;

    private String userId;

    private String postId;

    private String commentId;

    private LocalDateTime date;

}
