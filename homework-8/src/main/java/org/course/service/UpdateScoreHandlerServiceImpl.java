package org.course.service;

import lombok.AllArgsConstructor;
import org.course.repository.BookRepository;
import org.course.service.intefaces.UpdateScoreHandlerService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateScoreHandlerServiceImpl implements UpdateScoreHandlerService {

    private final BookRepository bookRepository;

    @Override
    public void updateScore(String bookId, int scoreValue){

        switch (scoreValue){
            case 1:
                bookRepository.increaseScoreOneCountById(bookId);
                break;
            case 2:
                bookRepository.increaseScoreTwoCountById(bookId);
                break;
            case 3:
                bookRepository.increaseScoreThreeCountById(bookId);
                break;
            case 4:
               bookRepository.increaseScoreFourCountById(bookId);
                break;
            case 5:
                bookRepository.increaseScoreFiveCountById(bookId);
                break;
            default:
                throw new IllegalArgumentException("Incorrect scoreValue " + scoreValue);
        }

    }

}
