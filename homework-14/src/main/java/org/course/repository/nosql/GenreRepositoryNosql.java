package org.course.repository.nosql;

import org.course.domain.nosql.GenreNosql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreRepositoryNosql extends MongoRepository<GenreNosql, String> {
}
