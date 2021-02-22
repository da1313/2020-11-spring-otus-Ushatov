package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.ScoreNumber;
import org.course.repository.BookRepository;
import org.course.service.intefaces.UpdateScoreHandlerService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateScoreHandlerServiceImpl implements UpdateScoreHandlerService {

    private final BookRepository bookRepository;

    @Override
    public void updateScore(String bookId, ScoreNumber scoreNumber){

        bookRepository.increaseScoreCount(bookId, scoreNumber);

    }

}
