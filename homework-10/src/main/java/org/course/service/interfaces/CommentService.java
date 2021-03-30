package org.course.service.interfaces;

import org.course.api.requests.CommentListRequest;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.domain.Comment;

public interface CommentService {
    CommentListResponse getComments(CommentListRequest request);

    Comment createComment(CommentRequest request);
}
