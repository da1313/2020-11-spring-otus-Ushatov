package org.course.dto.attributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.dto.state.BookPageParams;

import java.util.List;

@Data
@AllArgsConstructor
public class BookPageAttributes {

    private Book book;
    private List<Comment> commentList;
    private BookPageParams bookPageParams;

}
