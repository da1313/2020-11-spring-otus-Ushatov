package org.course.api.attributes;

import lombok.Data;
import org.course.domain.Book;
import org.course.domain.Comment;

import java.util.List;

@Data
public class BookPageAttributes {

    private final Book book;

    private final List<Comment> comments;

    private final PagingAttributes pagingAttributes;

}
