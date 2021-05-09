package org.course.contollers;

import lombok.RequiredArgsConstructor;
import org.course.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/api/books/{id}/take")
    public ResponseEntity<?> takeBook(@PathVariable("id") String bookId){
        return ResponseEntity.ok(cardService.takeBook(bookId));
    }

    @PostMapping("/api/books/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable("id") String bookId){
        return ResponseEntity.ok(cardService.returnBook(bookId));
    }

}
