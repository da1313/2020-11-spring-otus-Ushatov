package testdata;

import org.course.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestDataLoader implements ApplicationRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {

        ReactiveMongoOperations mongoOperations = applicationContext.getBean(ReactiveMongoOperations.class);

        //|book|author|genres   |comments|scores|
        //|B0  |A0    |G0,G1    |1       |1,2,3 |
        //|B1  |A1    |G0,G1,G2 |2       |3,3,3 |
        //|B2  |A2    |G1       |3       |5,2,1 |
        //|B3  |A0    |G0,G2    |0       |      |
        //|B4  |A2    |G1,G2    |0       |      |
        //|B5  |A1    |G2       |0       |      |

        List<Author> authorList = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        List<Book> bookList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Author author = Author.of("A" + i);
            mongoOperations.save(author).block();
            authorList.add(author);
        }

        for (int i = 0; i < 3; i++) {
            Genre genre = Genre.of("G" + i);
            mongoOperations.save(genre).block();
            genreList.add(genre);
        }

        for (int i = 0; i < 3; i++) {
            User user = User.of("UFN" + i, "ULN" + i);
            mongoOperations.save(user).block();
            userList.add(user);
        }

        Book book = Book.of("B0", authorList.get(0), List.of(genreList.get(0), genreList.get(1)));
        book.getInfo().setScoreOneCount(1);
        book.getInfo().setScoreTwoCount(1);
        book.getInfo().setScoreThreeCount(1);
        book.getInfo().setCommentCount(1);
        mongoOperations.save(book).block();
        bookList.add(book);

        book = Book.of("B1", authorList.get(1), List.of(genreList.get(0), genreList.get(1), genreList.get(2)));
        book.getInfo().setScoreThreeCount(3);
        book.getInfo().setCommentCount(2);
        mongoOperations.save(book).block();
        bookList.add(book);

        book = Book.of("B2", authorList.get(2), List.of(genreList.get(1)));
        book.getInfo().setScoreFiveCount(1);
        book.getInfo().setScoreTwoCount(1);
        book.getInfo().setScoreOneCount(1);
        book.getInfo().setCommentCount(3);
        mongoOperations.save(book).block();
        bookList.add(book);

        book = Book.of("B3", authorList.get(0), List.of(genreList.get(0), genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B4", authorList.get(2), List.of(genreList.get(1), genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B5", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);

        Comment comment = Comment.of("C0", userList.get(0), bookList.get(0));
        mongoOperations.save(comment).block();
        comment = Comment.of("C1", userList.get(1), bookList.get(1));
        mongoOperations.save(comment).block();
        comment = Comment.of("C2", userList.get(2), bookList.get(1));
        mongoOperations.save(comment).block();
        comment = Comment.of("C3", userList.get(0), bookList.get(2));
        mongoOperations.save(comment).block();
        comment = Comment.of("C4", userList.get(1), bookList.get(2));
        mongoOperations.save(comment).block();
        comment = Comment.of("C5", userList.get(0), bookList.get(2));
        mongoOperations.save(comment).block();

        Score score = Score.of(userList.get(0), bookList.get(0), 1);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(1), bookList.get(0), 2);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(2), bookList.get(0), 3);
        mongoOperations.save(score).block();

        score = Score.of(userList.get(0), bookList.get(1), 3);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(1), bookList.get(1), 3);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(2), bookList.get(1), 3);
        mongoOperations.save(score).block();

        score = Score.of(userList.get(0), bookList.get(2), 5);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(1), bookList.get(2), 2);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(2), bookList.get(2), 1);
        mongoOperations.save(score).block();

    }

}
