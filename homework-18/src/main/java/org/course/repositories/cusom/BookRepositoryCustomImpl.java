package org.course.repositories.cusom;

import com.mongodb.client.result.UpdateResult;
import org.course.domain.Book;
import org.course.domain.embedded.ScoreNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final ReactiveMongoOperations mongoOperations;
    private final Map<ScoreNumber, String> fieldMap = new HashMap<>();

    @Autowired
    public BookRepositoryCustomImpl(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;

        fieldMap.put(ScoreNumber.SCORE_ONE, "info.scoreOneCount");
        fieldMap.put(ScoreNumber.SCORE_TWO, "info.scoreTwoCount");
        fieldMap.put(ScoreNumber.SCORE_THREE, "info.scoreThreeCount");
        fieldMap.put(ScoreNumber.SCORE_FOUR, "info.scoreFourCount");
        fieldMap.put(ScoreNumber.SCORE_FIVE, "info.scoreFiveCount");
    }


    @Override
    public Mono<UpdateResult> increaseCommentCount(String bookId){

        return mongoOperations.updateFirst(Query.query(Criteria.where("id").is(bookId)),
                new Update().inc("info.commentCount", 1), Book.class);

    }

    @Override
    public Mono<UpdateResult> increaseScoreCount(String bookId, ScoreNumber scoreNumber) {

        return mongoOperations.updateFirst(Query.query(Criteria.where("id").is(bookId)),
                new Update().inc(fieldMap.get(scoreNumber), 1), Book.class);

    }

}
