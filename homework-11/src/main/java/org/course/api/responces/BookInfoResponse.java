package org.course.api.responces;

import lombok.Data;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookInfoResponse {

    private final String id;

    private final String title;

    private final LocalDateTime time;

    private final Author author;

    private final List<Genre> genres;

    private final List<Integer> scoreCounts;

    private final double avgScore;

    private final long commentCount;

    public static BookInfoResponse ofEmpty(){
        return new BookInfoResponse(null, null, null, null, null, null, 0, 0);
    }

    public static BookInfoResponse ofBook(Book book){
        return new BookInfoResponse(book.getId(), book.getTitle(), book.getTime(), book.getAuthor(), book.getGenres(),
                book.getInfo().getScoreCounts(), book.getInfo().getAvgScore(), book.getInfo().getCommentCount());
    }

}
