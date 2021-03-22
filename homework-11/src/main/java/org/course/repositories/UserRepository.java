package org.course.repositories;

import org.course.domain.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface UserRepository extends ReactiveSortingRepository<User, String> {
}
