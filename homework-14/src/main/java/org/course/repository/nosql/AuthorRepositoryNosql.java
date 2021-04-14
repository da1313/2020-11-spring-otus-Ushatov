package org.course.repository.nosql;

import org.course.domain.nosql.AuthorNosql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepositoryNosql extends MongoRepository<AuthorNosql, String> {
}