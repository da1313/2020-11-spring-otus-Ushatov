package org.course.repository.nosql;

import org.course.domain.nosql.BookNosql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepositoryNosql extends MongoRepository<BookNosql, String> {
}
