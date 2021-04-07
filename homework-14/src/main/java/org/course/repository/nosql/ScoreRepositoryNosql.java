package org.course.repository.nosql;

import org.course.domain.nosql.ScoreNosql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScoreRepositoryNosql extends MongoRepository<ScoreNosql, Long> {
}
