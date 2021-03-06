package org.course.converters;

import lombok.RequiredArgsConstructor;
import org.course.domain.nosql.BookNosql;
import org.course.domain.sql.Author;
import org.course.domain.sql.Book;
import org.course.domain.sql.BookInfo;
import org.course.domain.sql.Genre;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookBackwardEntityConverterImpl implements EntityConverter<BookNosql, Book>{

    private final KeyHolder<String, Long> keyHolder;

    @Override
    public Book convert(BookNosql input) {

        Long authorId = keyHolder.getKey(EntityName.AUTHOR, input.getAuthor().getId());

        long bookId = keyHolder.getNewKey(EntityName.BOOK);

        keyHolder.saveKey(EntityName.BOOK, input.getId(), bookId);

        Author author = new Author(authorId, input.getAuthor().getName());

        Set<Genre> genres = input.getGenres().stream()
                .map(g -> new Genre(keyHolder.getKey(EntityName.GENRE, g.getId()), g.getName())).collect(Collectors.toSet());

        return new Book(bookId, input.getTitle(), input.getTime(), input.getDescription(), author,
                new BookInfo(input.getInfo().getScoreOneCount(),
                        input.getInfo().getScoreTwoCount(),
                        input.getInfo().getScoreThreeCount(),
                        input.getInfo().getScoreFourCount(),
                        input.getInfo().getScoreFiveCount(),
                        input.getInfo().getCommentCount(),
                        input.getInfo().getAvgScore()), genres);
    }
}
