package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.dto.request.BookRequest;
import org.course.service.interfaces.BookHandleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@AllArgsConstructor
public class BookController {

    private final BookHandleService bookHandleService;

    @PostMapping("/book/create")
    public String createBook(BookRequest request){
        bookHandleService.createBook(request);
        return "redirect:/";
    }

    @PostMapping("/book/update/{id}")
    public String updateBook(@PathVariable("id") long id, BookRequest request){
        bookHandleService.updateBook(id, request);
        return "redirect:/";
    }

    @PostMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") long id){
        bookHandleService.deleteBook(id);
        return "redirect:/";
    }

}
