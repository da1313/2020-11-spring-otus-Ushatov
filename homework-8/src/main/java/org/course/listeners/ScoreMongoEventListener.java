package org.course.listeners;

import lombok.AllArgsConstructor;
import org.course.domain.Score;
import org.course.service.intefaces.UpdateScoreHandlerService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScoreMongoEventListener extends AbstractMongoEventListener<Score> {

    private final UpdateScoreHandlerService updateScoreHandler;

    @Override
    public void onAfterSave(AfterSaveEvent<Score> event) {

        Score score = event.getSource();

        updateScoreHandler.updateScore(score.getBook().getId(), score.getValue());

    }

}
