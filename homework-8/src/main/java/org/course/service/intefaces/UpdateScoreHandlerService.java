package org.course.service.intefaces;

import org.course.domain.ScoreNumber;

public interface UpdateScoreHandlerService {

    void updateScore(String bookId, ScoreNumber scoreNumber);

}
