package org.course.contollers;

import lombok.RequiredArgsConstructor;
import org.course.domain.Book;
import org.course.service.BookService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public CollectionModel<EntityModel<Book>> getBooks(){
        return bookService.getBooks();
    }

}
