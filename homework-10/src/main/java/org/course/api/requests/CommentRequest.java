package org.course.api.requests;

import lombok.Data;

@Data
public class CommentRequest {

    private final String id;

    private final String text;

}
