package org.course.jobs;

import org.assertj.core.api.Assertions;
import org.course.domain.nosql.*;
import org.course.domain.sql.*;
import org.course.repository.nosql.*;
import org.course.repository.sql.*;
import org.course.service.FakeNosqlDataHandler;
import org.course.service.FakeSqlDataHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("nosqlToSqlJop test")
@SpringBootTest
public class JobBackwardTest {

    public static final long PAGE_SIZE = 10L;
    public static final String PAGE_SIZE_NAME = "pageSize";
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private FakeSqlDataHandler fakeSqlDataHandler;

    @Autowired
    private FakeNosqlDataHandler fakeNosqlDataHandler;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private AuthorRepositoryNosql authorRepositoryNosql;

    @Autowired
    private GenreRepositoryNosql genreRepositoryNosql;

    @Autowired
    private BookRepositoryNosql bookRepositoryNosql;

    @Autowired
    private CommentRepositoryNosql commentRepositoryNosql;

    @Autowired
    private ScoreRepositoryNosql scoreRepositoryNosql;

    @Autowired
    @Qualifier("nosqlToSqlJob")
    private Job nosqlToSqlJob;

    @BeforeEach
    private void initTestUtils(){
        jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobRepository(jobRepository);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJob(nosqlToSqlJob);
    }

    @Test
    void shouldConvertMongoDatabaseToSql() throws Exception {
        //generated data has unique field content
        fakeNosqlDataHandler.clearData();
        fakeSqlDataHandler.clearData();
        fakeNosqlDataHandler.initData();

        JobParameters jobParameters = new JobParametersBuilder().addLong(PAGE_SIZE_NAME, PAGE_SIZE).toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assertions.assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        //authors
        List<String> authorNamesSql = authorRepository.findAll().stream().map(Author::getName).collect(Collectors.toList());

        List<String> authorNamesNosql = authorRepositoryNosql.findAll().stream().map(AuthorNosql::getName).collect(Collectors.toList());

        Assertions.assertThat(authorNamesSql.size()).isEqualTo(authorNamesNosql.size());

        Assertions.assertThat(authorNamesSql).containsExactlyInAnyOrderElementsOf(authorNamesNosql);
        //Genres
        List<String> genresNamesSql = genreRepository.findAll().stream().map(Genre::getName).collect(Collectors.toList());

        List<String> genresNamesNosql = genreRepositoryNosql.findAll().stream().map(GenreNosql::getName).collect(Collectors.toList());

        Assertions.assertThat(genresNamesSql.size()).isEqualTo(genresNamesNosql.size());

        Assertions.assertThat(genresNamesSql).containsExactlyInAnyOrderElementsOf(genresNamesNosql);
        //Books
        List<Book> bookList = bookRepository.findAllEager();
        List<BookNosql> bookNosqlList = bookRepositoryNosql.findAll();
        for (int i = 0; i < bookList.size(); i++) {
            System.out.println(bookList.get(i) + "--" + bookList.get(i).getAuthor().getName() + "--" + bookList.get(i).getGenres());
            System.out.println(bookNosqlList.get(i) + "--" + bookNosqlList.get(i).getAuthor().getName() + "--" + bookNosqlList.get(i).getGenres());
        }

        Assertions.assertThat(bookList.size()).isEqualTo(bookNosqlList.size());

        bookList.forEach(b -> Assertions.assertThat(
                bookEquals(b, bookNosqlList.stream().filter(bookNosql -> bookNosql.getTitle().equals(b.getTitle()))
                        .findFirst().orElseThrow())).isTrue());
        //Comments
        List<Comment> commentList = commentRepository.findAllEager();
        List<CommentNosql> commentNosqlList = commentRepositoryNosql.findAll();
        for (int i = 0; i < commentList.size(); i++) {
            System.out.println(commentList.get(i) + "--" + commentList.get(i).getUser().getName() + "--" + commentList.get(i).getBook().getTitle());
            System.out.println(commentNosqlList.get(i)+ "--" + commentNosqlList.get(i).getUser().getName() + "--" + commentNosqlList.get(i).getBook().getTitle());
        }

        Assertions.assertThat(commentList.size()).isEqualTo(commentNosqlList.size());
        commentNosqlList.forEach(commentNosql -> Assertions.assertThat(commentEquals(commentList.stream()
                .filter(c -> c.getText().equals(commentNosql.getText())).findFirst().orElseThrow(), commentNosql)).isTrue());
        //Scores
        List<Score> scoreList = scoreRepository.findAllEager();
        List<ScoreNosql> scoreNosqlList = scoreRepositoryNosql.findAll();
        for (int i = 0; i < scoreList.size(); i++) {
            System.out.println(scoreList.get(i) + "--" + scoreList.get(i).getUser().getName() + "--" + scoreList.get(i).getBook().getTitle());
            System.out.println(scoreNosqlList.get(i) + "--" + scoreNosqlList.get(i).getUser().getName() + "--" + scoreNosqlList.get(i).getBook().getTitle());
        }

        Assertions.assertThat(scoreList.size()).isEqualTo(scoreNosqlList.size());
        List<ScoreNosql> collect = scoreNosqlList.stream().filter(scoreNosql -> scoreList.stream()
                .filter(s -> scoreEquals(s, scoreNosql)).count() != 1).collect(Collectors.toList());
        Assertions.assertThat(collect.size()).isEqualTo(0);
    }

    private boolean bookEquals(Book book, BookNosql bookNosql){
        if (!book.getTitle().equals(bookNosql.getTitle())) return false;
//        if (book.getTime().getNano() != bookNosql.getTime().getNano()) return false;
        if (!book.getDescription().equals(bookNosql.getDescription())) return false;
        if (!book.getAuthor().getName().equals(bookNosql.getAuthor().getName())) return false;
        if (book.getBookInfo().getScoreOneCount() != bookNosql.getInfo().getScoreOneCount()) return false;
        if (book.getBookInfo().getScoreTwoCount() != bookNosql.getInfo().getScoreTwoCount()) return false;
        if (book.getBookInfo().getScoreThreeCount() != bookNosql.getInfo().getScoreThreeCount()) return false;
        if (book.getBookInfo().getScoreFourCount() != bookNosql.getInfo().getScoreFourCount()) return false;
        if (book.getBookInfo().getScoreFiveCount() != bookNosql.getInfo().getScoreFiveCount()) return false;
        if (book.getBookInfo().getCommentCount() != bookNosql.getInfo().getCommentCount()) return false;
        if (book.getBookInfo().getAvgScore() != bookNosql.getInfo().getAvgScore()) return false;
        if (book.getGenres().size() != bookNosql.getGenres().size()) return false;
        return book.getGenres().stream().noneMatch(g -> bookNosql.getGenres().stream().filter(genreNosql -> genreEquals(g, genreNosql)).count() != 1);
    }

    private boolean genreEquals(Genre genre, GenreNosql genreNosql){
        return genre.getName().equals(genreNosql.getName());
    }

    private boolean commentEquals(Comment comment, CommentNosql commentNosql){
        if (!comment.getText().equals(commentNosql.getText())) return false;
        if (!commentNosql.getBook().getTitle().equals(comment.getBook().getTitle())) return false;
        return commentNosql.getUser().getName().equals(comment.getUser().getName());
    }

    private boolean scoreEquals(Score score, ScoreNosql scoreNosql){
        if (scoreNosql.getValue() != score.getScore()) return false;
        if (!scoreNosql.getBook().getTitle().equals(score.getBook().getTitle())) return false;
        return scoreNosql.getUser().getName().equals(score.getUser().getName());
    }

}
