package org.course.api.responces;

import lombok.Data;
import org.course.api.pojo.CommentShort;
import org.course.domain.Comment;

import java.util.List;

@Data
public class CommentListResponse {

    private final List<CommentShort> commentList;

    private final int totalPages;

}
