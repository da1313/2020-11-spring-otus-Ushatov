package org.course.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import org.course.domain.*;

import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class TestDataInitializer {

    @ChangeSet(order = "000", id = "dropDb", author = "author", runAlways = true)
    public void dropDb(MongoDatabase mongoDatabase){
        mongoDatabase.drop();
    }

    @ChangeSet(order = "001", id = "addData", author = "author", runAlways = true)
    public void addData(MongockTemplate mongockTemplate){

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
            mongockTemplate.save(author);
            authorList.add(author);
        }

        for (int i = 0; i < 3; i++) {
            Genre genre = Genre.of("G" + i);
            mongockTemplate.save(genre);
            genreList.add(genre);
        }

        for (int i = 0; i < 3; i++) {
            User user = User.of("UFN" + i, "ULN" + i);
            mongockTemplate.save(user);
            userList.add(user);
        }

        Book book = Book.of("B0", authorList.get(0), List.of(genreList.get(0), genreList.get(1)));
        book.getInfo().setScoreOneCount(1);
        book.getInfo().setScoreTwoCount(1);
        book.getInfo().setScoreThreeCount(1);
        book.getInfo().setCommentCount(1);
        mongockTemplate.save(book);
        bookList.add(book);

        book = Book.of("B1", authorList.get(1), List.of(genreList.get(0), genreList.get(1), genreList.get(2)));
        book.getInfo().setScoreThreeCount(3);
        book.getInfo().setCommentCount(2);
        mongockTemplate.save(book);
        bookList.add(book);

        book = Book.of("B2", authorList.get(2), List.of(genreList.get(1)));
        book.getInfo().setScoreFiveCount(1);
        book.getInfo().setScoreTwoCount(1);
        book.getInfo().setScoreOneCount(1);
        book.getInfo().setCommentCount(3);
        mongockTemplate.save(book);
        bookList.add(book);

        book = Book.of("B3", authorList.get(0), List.of(genreList.get(0), genreList.get(2)));
        mongockTemplate.save(book);
        bookList.add(book);
        book = Book.of("B4", authorList.get(2), List.of(genreList.get(1), genreList.get(2)));
        mongockTemplate.save(book);
        bookList.add(book);
        book = Book.of("B5", authorList.get(1), List.of(genreList.get(2)));
        mongockTemplate.save(book);
        bookList.add(book);

        Comment comment = Comment.of("C0", userList.get(0), bookList.get(0));
        mongockTemplate.save(comment);
        comment = Comment.of("C1", userList.get(1), bookList.get(1));
        mongockTemplate.save(comment);
        comment = Comment.of("C2", userList.get(2), bookList.get(1));
        mongockTemplate.save(comment);
        comment = Comment.of("C3", userList.get(0), bookList.get(2));
        mongockTemplate.save(comment);
        comment = Comment.of("C4", userList.get(1), bookList.get(2));
        mongockTemplate.save(comment);
        comment = Comment.of("C5", userList.get(0), bookList.get(2));
        mongockTemplate.save(comment);

        Score score = Score.of(userList.get(0), bookList.get(0), 1);
        mongockTemplate.save(score);
        score = Score.of(userList.get(1), bookList.get(0), 2);
        mongockTemplate.save(score);
        score = Score.of(userList.get(2), bookList.get(0), 3);
        mongockTemplate.save(score);

        score = Score.of(userList.get(0), bookList.get(1), 3);
        mongockTemplate.save(score);
        score = Score.of(userList.get(1), bookList.get(1), 3);
        mongockTemplate.save(score);
        score = Score.of(userList.get(2), bookList.get(1), 3);
        mongockTemplate.save(score);

        score = Score.of(userList.get(0), bookList.get(2), 5);
        mongockTemplate.save(score);
        score = Score.of(userList.get(1), bookList.get(2), 2);
        mongockTemplate.save(score);
        score = Score.of(userList.get(2), bookList.get(2), 1);
        mongockTemplate.save(score);

    }

}
