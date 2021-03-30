package org.course.service;

import lombok.AllArgsConstructor;
import org.course.api.request.ScoreRequest;
import org.course.domain.Book;
import org.course.domain.Score;
import org.course.domain.User;
import org.course.exception.EntityNotFoundException;
import org.course.repository.BookRepository;
import org.course.repository.ScoreRepository;
import org.course.service.interfaces.ScoreService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;

    private final BookRepository bookRepository;

    @Override
    public void createScore(ScoreRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + request.getBookId() + " not found!"));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        scoreRepository.findByUserAndBook(user, book).ifPresentOrElse(score -> {
            int value = score.getScore();
            score.setScore(request.getValue());
            scoreRepository.save(score);
            book.getInfo().removeScore(value);
            book.getInfo().addScore(request.getValue());
            book.getInfo().setAvgScore();
            bookRepository.save(book);
        }, () -> {
            Score score = new Score(0, request.getValue(), user, book);
            scoreRepository.save(score);
            book.getInfo().addScore(request.getValue());
            book.getInfo().setAvgScore();
            bookRepository.save(book);
        });

    }
}
