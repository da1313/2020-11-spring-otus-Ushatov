package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.contollers.BookController;
import org.course.contollers.CardController;
import org.course.domain.Book;
import org.course.domain.Card;
import org.course.domain.User;
import org.course.repository.BookRepository;
import org.course.repository.CardRepository;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CardRepository cardRepository;

    private final RepositoryEntityLinks repositoryEntityLinks;

    @Override
    public CollectionModel<EntityModel<Book>> getBooks() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Book> bookList = bookRepository.findAll();
        List<Card> cardList = cardRepository.findByUser(user);

        return CollectionModel.of(bookList.stream()
                .map(b -> processBook(b, cardList)).collect(Collectors.toList()))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).getBooks()).withRel("books"));
    }

    private EntityModel<Book> processBook(Book book, List<Card> cardList){
        EntityModel<Book> bookEntityModel = EntityModel.of(book);
        if (cardList.stream().anyMatch(c -> c.getBook().getId().equals(book.getId()))){
            bookEntityModel
                    .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                            .methodOn(CardController.class).returnBook(book.getId())).withRel("return"));
        } else {
            if (book.getQuantity() != 0){
                bookEntityModel
                        .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                                .methodOn(CardController.class).takeBook(book.getId())).withRel("take"));
            }
        }
        bookEntityModel.add(repositoryEntityLinks.linkForItemResource(Book.class, book.getId()).withSelfRel());
        return bookEntityModel;
    }

}
