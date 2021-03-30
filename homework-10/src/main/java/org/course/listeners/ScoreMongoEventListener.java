package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.Score;
import org.course.domain.ScoreNumber;
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

        bookRepository.increaseScoreCount(score.getBook().getId(), ScoreNumber.of(score.getValue()));


    }

}
