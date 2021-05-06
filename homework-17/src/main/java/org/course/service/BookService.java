package org.course.service;

import org.course.domain.Book;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface BookService {

    CollectionModel<EntityModel<Book>> getBooks();

}
