package org.course;

import com.mongodb.MongoTimeoutException;
import com.mongodb.reactivestreams.client.MongoClient;
import org.course.domain.*;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
@DependsOn("reactiveMongoTemplate")
@ConditionalOnProperty(name = "init", havingValue = "production", prefix = "app")
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @Autowired
    private MongoClient mongoClient;

    static class ObservableSubscriber<T> implements Subscriber<T> {
        private final List<T> received;
        private final List<Throwable> errors;
        private final CountDownLatch latch;
        private volatile Subscription subscription;
        private volatile boolean completed;

        ObservableSubscriber() {
            this.received = new ArrayList<>();
            this.errors = new ArrayList<>();
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void onSubscribe(final Subscription s) {
            subscription = s;
        }

        @Override
        public void onNext(final T t) {
            received.add(t);
        }

        @Override
        public void onError(final Throwable t) {
            errors.add(t);
            onComplete();
        }

        @Override
        public void onComplete() {
            completed = true;
            latch.countDown();
        }

        public Subscription getSubscription() {
            return subscription;
        }

        public List<T> getReceived() {
            return received;
        }

        public Throwable getError() {
            if (errors.size() > 0) {
                return errors.get(0);
            }
            return null;
        }

        public boolean isCompleted() {
            return completed;
        }

        public List<T> get(final long timeout, final TimeUnit unit) throws Throwable {
            return await(timeout, unit).getReceived();
        }

        public ObservableSubscriber<T> await() throws Throwable {
            return await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        }

        public ObservableSubscriber<T> await(final long timeout, final TimeUnit unit) throws Throwable {
            subscription.request(Integer.MAX_VALUE);
            if (!latch.await(timeout, unit)) {
                throw new MongoTimeoutException("Publisher onComplete timed out");
            }
            if (!errors.isEmpty()) {
                throw errors.get(0);
            }
            return this;
        }
    }

    @Override
    public void run(ApplicationArguments args) {

        ObservableSubscriber<Void> subscriber = new ObservableSubscriber<>();

        mongoClient.getDatabase("lib").drop().subscribe(subscriber);

        try {
            subscriber.await();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

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
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B2", authorList.get(2), List.of(genreList.get(1)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B3", authorList.get(0), List.of(genreList.get(0), genreList.get(2)));
        book.getInfo().setCommentCount(3);
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B4", authorList.get(2), List.of(genreList.get(1), genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B5", authorList.get(1), List.of(genreList.get(2)));
        book.getInfo().setScoreFiveCount(2);
        book.getInfo().setScoreFourCount(1);
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B6", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B7", authorList.get(1), List.of(genreList.get(2)));
        book.getInfo().setCommentCount(2);
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B8", authorList.get(1), List.of(genreList.get(2)));
        book.getInfo().setScoreThreeCount(3);
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B9", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B10", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B11", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B12", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B13", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B14", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);
        book = Book.of("B15", authorList.get(1), List.of(genreList.get(2)));
        mongoOperations.save(book).block();
        bookList.add(book);

        Comment comment = Comment.of("C0", userList.get(0), bookList.get(3));
        mongoOperations.save(comment).block();
        comment = Comment.of("C1", userList.get(1), bookList.get(3));
        mongoOperations.save(comment).block();
        comment = Comment.of("C2", userList.get(2), bookList.get(3));
        mongoOperations.save(comment).block();
        comment = Comment.of("C3", userList.get(0), bookList.get(7));
        mongoOperations.save(comment).block();
        comment = Comment.of("C4", userList.get(1), bookList.get(7));
        mongoOperations.save(comment).block();
        comment = Comment.of("C5", userList.get(0), bookList.get(1));
        mongoOperations.save(comment).block();

        Score score = Score.of(userList.get(0), bookList.get(5), 5);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(1), bookList.get(5), 4);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(2), bookList.get(5), 5);
        mongoOperations.save(score).block();

        score = Score.of(userList.get(0), bookList.get(8), 3);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(1), bookList.get(8), 3);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(2), bookList.get(8), 3);
        mongoOperations.save(score).block();

        score = Score.of(userList.get(0), bookList.get(1), 3);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(1), bookList.get(1), 2);
        mongoOperations.save(score).block();
        score = Score.of(userList.get(2), bookList.get(1), 1);
        mongoOperations.save(score).block();

    }
}
