package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.Book;
import org.course.domain.Score;
import org.course.repository.BookRepository;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScoreMongoEventListener extends AbstractMongoEventListener<Score> {

    private final BookRepository bookRepository;

    @Override
    public void onAfterSave(AfterSaveEvent<Score> event) {

        Score score = event.getSource();

        Book book = bookRepository.findById(score.getBook().getId())
                .orElseThrow(() -> new IllegalArgumentException("Book whit id " + score.getBook().getId() + " not found!"));

        switch (score.getValue()){
            case 1 :
                book.getInfo().setOne(book.getInfo().getOne() + 1);
                break;
            case 2:
                book.getInfo().setTwo(book.getInfo().getTwo() + 1);
                break;
            case 3:
                book.getInfo().setThree(book.getInfo().getThree() + 1);
                break;
            case 4:
                book.getInfo().setFour(book.getInfo().getFour() + 1);
                break;
            case 5:
                book.getInfo().setFive(book.getInfo().getFive() + 1);
                break;
        }

        book.getInfo().calculate();

        bookRepository.save(book);

    }

}
