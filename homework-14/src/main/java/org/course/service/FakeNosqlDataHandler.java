package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.nosql.*;
import org.course.domain.nosql.embedded.BookEmbedded;
import org.course.domain.nosql.embedded.Info;
import org.course.domain.nosql.embedded.UserEmbedded;
import org.course.domain.sql.Book;
import org.course.domain.sql.Comment;
import org.course.domain.sql.Score;
import org.course.domain.sql.User;
import org.course.repository.nosql.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@AllArgsConstructor
public class FakeNosqlDataHandler {

    private final BookRepositoryNosql bookRepository;
    private final AuthorRepositoryNosql authorRepository;
    private final ScoreRepositoryNosql scoreRepository;
    private final CommentRepositoryNosql commentRepository;
    private final UserRepositoryNosql userRepository;
    private final GenreRepositoryNosql genreRepository;



    public void clearData(){

        bookRepository.deleteAll();
        authorRepository.deleteAll();
        scoreRepository.deleteAll();
        commentRepository.deleteAll();
        userRepository.deleteAll();
        genreRepository.deleteAll();

    }

    public void initData(){

        List<AuthorNosql> authorList = new ArrayList<>();
        List<BookNosql> bookList = new ArrayList<>();
        List<CommentNosql> commentList = new ArrayList<>();
        List<ScoreNosql> scoreList = new ArrayList<>();
        List<UserNosql> userList = new ArrayList<>();
        List<GenreNosql> genreList = new ArrayList<>();

        int authorCount = 10;
        int userCount = 5;
        int bookCount = 20;
        int commentCount = 10;
        int scoreCount = 10;
        int genreCount = 5;
        int k = 0;

        Random random = new Random();

        for (int i = 0; i < authorCount; i++) {
            AuthorNosql author = new AuthorNosql(null, "A" + i);
            authorList.add(authorRepository.save(author));
        }

        for (int i = 0; i < userCount; i++) {
            UserNosql user = new UserNosql(null, "U" + i, "P" + i, true);
            userList.add(userRepository.save(user));
        }

        for (int i = 0; i < genreCount; i++) {
            GenreNosql genre = new GenreNosql(null, "G" + i);
            genreList.add(genreRepository.save(genre));
        }

        for (int i = 0; i < bookCount; i++) {
            AuthorNosql author = authorList.get(random.nextInt(authorCount));

            Set<GenreNosql> genreSet = new HashSet<>();
            for (int j = 0; j < random.nextInt(genreCount); j++) {
                genreSet.add(genreList.get(random.nextInt(genreCount)));
            }

            BookNosql book = new BookNosql(null, "T" + i, LocalDateTime.now(), "D" + i, author, new ArrayList<>(genreSet), new Info());
            bookList.add(bookRepository.save(book));
        }

        for (int i = 0; i < scoreCount; i++) {
            BookNosql book = bookList.get(random.nextInt(bookCount));
            int value = random.nextInt(5) + 1;
            book.getInfo().addScore(value);
            book.getInfo().setAvgScore();

            UserNosql user = userList.get(random.nextInt(userCount));

            UserEmbedded userEmbedded = new UserEmbedded(user.getId(), user.getName());
            BookEmbedded bookEmbedded = new BookEmbedded(book.getId(), book.getTitle());

            ScoreNosql score = new ScoreNosql(null, userEmbedded, bookEmbedded, value);

            bookRepository.save(book);
            scoreList.add(scoreRepository.save(score));
        }

        while (k < commentCount){
            BookNosql book = bookList.get(random.nextInt(bookCount));
            UserNosql user = userList.get(random.nextInt(userCount));
            if (commentList.stream().anyMatch(s -> s.getUser().getId().equals(user.getId()) && s.getBook().getId().equals(book.getId()))) continue;
            book.getInfo().setCommentCount(book.getInfo().getCommentCount() + 1);
            UserEmbedded userEmbedded = new UserEmbedded(user.getId(), user.getName());
            BookEmbedded bookEmbedded = new BookEmbedded(book.getId(), book.getTitle());

            CommentNosql comment = new CommentNosql(null, "T" + k, LocalDateTime.now(), userEmbedded, bookEmbedded);

            bookRepository.save(book);
            commentList.add(commentRepository.save(comment));
            k++;
        }

        k = 0;
        while (k < scoreCount){
            BookNosql book = bookList.get(random.nextInt(bookCount));
            UserNosql user = userList.get(random.nextInt(userCount));
            if (scoreList.stream().anyMatch(s -> s.getUser().getId().equals(user.getId()) && s.getBook().getId().equals(book.getId()))) continue;
            int value = random.nextInt(5) + 1;
            book.getInfo().addScore(value);
            book.getInfo().setAvgScore();
            UserEmbedded userEmbedded = new UserEmbedded(user.getId(), user.getName());
            BookEmbedded bookEmbedded = new BookEmbedded(book.getId(), book.getTitle());

            ScoreNosql score = new ScoreNosql(null, userEmbedded, bookEmbedded, value);

            bookRepository.save(book);
            scoreList.add(scoreRepository.save(score));
            k++;
        }
    }

}
