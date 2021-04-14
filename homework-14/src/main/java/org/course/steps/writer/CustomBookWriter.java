package org.course.steps.writer;

import lombok.RequiredArgsConstructor;
import org.course.domain.sql.Book;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomBookWriter implements ItemWriter<Book> {

    private final JdbcOperations jdbcOperations;

    @Override
    public void write(List<? extends Book> items) throws Exception {

        jdbcOperations.batchUpdate("insert into books(id, title, time, description, author_id, score_one_count," +
                " score_two_count, score_three_count, score_four_count, score_five_count, comment_count, avg_score) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new BookPrepareStatementSetter(items));

        List<Object[]> relations = items.stream().flatMap(i -> i.getGenres().stream()
                .map(g -> new Object[]{i.getId(), g.getId()})).collect(Collectors.toList());

        jdbcOperations.batchUpdate("insert into books_to_genres(book_id, genre_id)" +
                " values(?, ?)", relations);

    }

    static class BookPrepareStatementSetter implements BatchPreparedStatementSetter{

        private final List<? extends Book> items;

        BookPrepareStatementSetter(List<? extends Book> items) {
            this.items = items;
        }

        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
            Book book = items.get(i);
            ps.setLong(1, book.getId());
            ps.setString(2, book.getTitle());
            ps.setTimestamp(3, Timestamp.valueOf(book.getTime()));
            ps.setString(4, book.getDescription());
            ps.setLong(5, book.getAuthor().getId());
            ps.setInt(6, book.getBookInfo().getScoreOneCount());
            ps.setInt(7, book.getBookInfo().getScoreTwoCount());
            ps.setInt(8, book.getBookInfo().getScoreThreeCount());
            ps.setInt(9, book.getBookInfo().getScoreFourCount());
            ps.setInt(10, book.getBookInfo().getScoreFiveCount());
            ps.setInt(11, book.getBookInfo().getCommentCount());
            ps.setDouble(12, book.getBookInfo().getAvgScore());
        }

        @Override
        public int getBatchSize() {
            return items.size();
        }
    }
}
