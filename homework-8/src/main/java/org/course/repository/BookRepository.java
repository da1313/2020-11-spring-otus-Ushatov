package org.course.repository;

import org.course.domain.Book;
import org.course.repository.custom.BookRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {

}
