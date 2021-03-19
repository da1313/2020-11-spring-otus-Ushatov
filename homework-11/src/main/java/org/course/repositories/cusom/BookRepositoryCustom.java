package org.course.repositories.cusom;

import com.mongodb.client.result.UpdateResult;
import org.course.domain.embedded.ScoreNumber;
import reactor.core.publisher.Mono;

public interface BookRepositoryCustom {

    Mono<UpdateResult> increaseCommentCount(String bookId);

    Mono<UpdateResult> increaseScoreCount(String bookId, ScoreNumber scoreNumber);

}
