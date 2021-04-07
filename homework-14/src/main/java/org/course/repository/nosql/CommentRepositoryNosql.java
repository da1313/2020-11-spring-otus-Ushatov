package org.course.repository.nosql;

import org.course.domain.nosql.CommentNosql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepositoryNosql extends MongoRepository<CommentNosql, Long> {
}
