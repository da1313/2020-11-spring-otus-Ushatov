package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.api.request.BookRequest;
import org.course.service.interfaces.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes(names = {"managePage"})
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/manage/book/delete/{id}")
    public String deleteBook(@PathVariable("id") long id, Model model, RedirectAttributes attributes){
        Integer managePage = (Integer) model.getAttribute("managePage");
        bookService.deleteBook(id);
        attributes.addAttribute("page", managePage);
        return "redirect:/manage";
    }

    @PostMapping("/manage/book/update/{id}")
    public String updateBook(@PathVariable("id") long id, BookRequest bookRequest, Model model,
                             RedirectAttributes attributes){
        Integer managePage = (Integer) model.getAttribute("managePage");
        bookService.updateBook(id, bookRequest);
        attributes.addAttribute("page", managePage);
        return "redirect:/manage";
    }

    @PostMapping("/manage/book/create")
    public String createBook(BookRequest bookRequest, Model model, RedirectAttributes attributes){
        Integer managePage = (Integer) model.getAttribute("managePage");
        bookService.createBook(bookRequest);
        attributes.addAttribute("page", managePage);
        return "redirect:/manage";
    }

}
