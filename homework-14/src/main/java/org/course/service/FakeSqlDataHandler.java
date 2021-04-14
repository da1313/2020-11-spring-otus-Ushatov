package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.sql.*;
import org.course.repository.sql.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;

@Component
@AllArgsConstructor
public class FakeSqlDataHandler {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ScoreRepository scoreRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;

    @Transactional
    public void clearData(){

        bookRepository.deleteAll();
        authorRepository.deleteAll();
        scoreRepository.deleteAll();
        commentRepository.deleteAll();
        userRepository.deleteAll();
        genreRepository.deleteAll();

    }

    @Transactional
    public void initData(){

        List<Author> authorList = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();
        List<Comment> commentList = new ArrayList<>();
        List<Score> scoreList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();

        int authorCount = 10;
        int userCount = 5;
        int bookCount = 200;
        int commentCount = 10;
        int scoreCount = 10;
        int genreCount = 5;
        int k = 0;

        Random random = new Random();

        for (int i = 0; i < authorCount; i++) {
            Author author = new Author(0, "A" + i);
            authorList.add(authorRepository.save(author));
        }

        for (int i = 0; i < userCount; i++) {
            User user = new User(0, "U" + i, "P" + i, true);

            userList.add(userRepository.save(user));
        }

        for (int i = 0; i < genreCount; i++) {
            Genre genre = new Genre(0, "G" + i);
            genreList.add(genreRepository.save(genre));
        }

        for (int i = 0; i < bookCount; i++) {
            Author author = authorList.get(random.nextInt(authorCount));

            Set<Genre> genreSet = new HashSet<>();
            for (int j = 0; j < random.nextInt(genreCount); j++) {
                genreSet.add(genreList.get(random.nextInt(genreCount)));
            }

            Book book = new Book(0, "T" + i, LocalDateTime.now(), "D" + i, author, new BookInfo(), new HashSet<>());

            bookList.add(bookRepository.save(book));

            genreSet.forEach(g -> book.getGenres().add(g));

            bookRepository.save(book);
        }

        while (k < commentCount){
            Book book = bookList.get(random.nextInt(bookCount));
            User user = userList.get(random.nextInt(userCount));
            if (commentList.stream().anyMatch(s -> s.getUser().getId() == user.getId() && s.getBook().getId() == book.getId())) continue;
            book.getBookInfo().setCommentCount(book.getBookInfo().getCommentCount() + 1);
            Comment comment = new Comment(0, "T" + k, LocalDateTime.now(), book, user);
            bookRepository.save(book);
            commentList.add(commentRepository.save(comment));
            k++;
        }

        k = 0;
        while (k < scoreCount){
            Book book = bookList.get(random.nextInt(bookCount));
            User user = userList.get(random.nextInt(userCount));
            if (scoreList.stream().anyMatch(s -> s.getUser().getId() == user.getId() && s.getBook().getId() == book.getId())) continue;
            int value = random.nextInt(5) + 1;
            Score score = new Score(0, value, user, book);
            book.getBookInfo().addScore(value);
            book.getBookInfo().setAvgScore();
            bookRepository.save(book);
            scoreList.add(scoreRepository.save(score));
            k++;
        }
    }

}
