package org.course.api.requests;

import lombok.Data;

@Data
public class CommentListRequest {

    private final String id;

    private final int pageSize;

    private final int pageNumber;

}
