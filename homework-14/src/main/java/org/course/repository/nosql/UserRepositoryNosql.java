package org.course.repository.nosql;

import org.course.domain.nosql.UserNosql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepositoryNosql extends MongoRepository<UserNosql, String> {
}
