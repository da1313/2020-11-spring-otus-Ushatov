package org.course.api.request;

import lombok.Data;

@Data
public class CommentRequest {

    private final long bookId;

    private final String text;

}
