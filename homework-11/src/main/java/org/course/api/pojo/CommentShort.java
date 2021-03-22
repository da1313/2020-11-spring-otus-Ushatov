package org.course.api.pojo;

import lombok.Data;

@Data
public class CommentShort {

    private final String id;

    private final String text;

    private final String username;

}
