package org.course.converters;

import org.course.domain.nosql.AuthorNosql;
import org.course.domain.nosql.BookNosql;
import org.course.domain.nosql.GenreNosql;
import org.course.domain.nosql.embedded.Info;
import org.course.domain.sql.Book;
import org.course.keyholder.EntityName;
import org.course.keyholder.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookForwardEntityConverterImpl implements EntityConverter<Book, BookNosql>{

    private static final String JOB_NAME = "sqlToNosqlJob";

    private final List<KeyHolder<Long, String>> keyHolderList;

    private final KeyHolder<Long, String> keyHolder;

    public BookForwardEntityConverterImpl(@Autowired List<KeyHolder<Long, String>> keyHolderList) {
        this.keyHolderList = keyHolderList;
        keyHolder = keyHolderList.stream().filter(k -> k.getJob().equals(JOB_NAME)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find job with name " + JOB_NAME + " in keyholder declaration"));
    }

    @Override
    public BookNosql convert(Book input) {
        String authorId = keyHolder.getKey(EntityName.AUTHOR, input.getAuthor().getId());

        String bookId = keyHolder.getNewKey(EntityName.BOOK);

        keyHolder.saveKey(EntityName.BOOK, input.getId(), bookId);

        AuthorNosql authorNosql = new AuthorNosql(authorId, input.getAuthor().getName());

        List<GenreNosql> genres = input.getGenres().stream()
                .map(g -> new GenreNosql(keyHolder.getKey(EntityName.GENRE, g.getId()), g.getName())).collect(Collectors.toList());

        return new BookNosql(bookId, input.getTitle(), input.getTime(), input.getDescription(), authorNosql, genres,
                new Info(input.getBookInfo().getScoreOneCount(),
                        input.getBookInfo().getScoreTwoCount(),
                        input.getBookInfo().getScoreThreeCount(),
                        input.getBookInfo().getScoreFourCount(),
                        input.getBookInfo().getScoreFiveCount(),
                        input.getBookInfo().getCommentCount(),
                        input.getBookInfo().getAvgScore()));
    }
}
