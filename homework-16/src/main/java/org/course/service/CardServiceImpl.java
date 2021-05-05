package org.course.service;

import lombok.RequiredArgsConstructor;
import org.course.domain.Book;
import org.course.domain.Card;
import org.course.domain.User;
import org.course.dto.GenericResponse;
import org.course.exceptions.EntityNotFoundException;
import org.course.repository.BookRepository;
import org.course.repository.CardRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final BookRepository bookRepository;

    private final CardRepository cardRepository;

    @Transactional
    @Override
    public GenericResponse takeBook(String bookId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("The book with id " + bookId + " not found!"));

        return cardRepository.findByUserAndBook(user, book)
                .map(b -> GenericResponse.withMessage(false, "The book already taken by user"))
                .orElseGet(() -> {
                    if (book.getQuantity() == 0) {
                        return GenericResponse.withMessage(false, "Not enough books!");
                    } else {
                        return getSuccessfulTakeResponse(user, book);
                    }
                });
    }

    @Transactional
    @Override
    public GenericResponse returnBook(String bookId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("The book with id " + bookId + " not found!"));

        return cardRepository.findByUserAndBook(user, book).map(card -> getSuccessfulReturnResponse(card, book))
                .orElseGet(() -> GenericResponse.withMessage(false, "User don't take the book!"));

    }

    private GenericResponse getSuccessfulReturnResponse(Card card, Book book){
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);
        cardRepository.delete(card);
        return GenericResponse.withResult(true);
    }

    private GenericResponse getSuccessfulTakeResponse(User user, Book book) {
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);
        Card card = new Card(null, user, book, LocalDateTime.now(), LocalDateTime.now().plus(2, ChronoUnit.WEEKS));
        cardRepository.save(card);
        return GenericResponse.withResult(true);
    }

}
