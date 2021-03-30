package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.api.attributes.BookPageAttributes;
import org.course.api.attributes.BooksPageAttributes;
import org.course.api.attributes.UpdateBookPageAttributes;
import org.course.domain.User;
import org.course.service.interfaces.PagesService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = {"managePage"})
@AllArgsConstructor
public class PageController {

    private final PagesService pagesService;

    @GetMapping("/login")
    public String getLoginView(){
        return "login";
    }

    @GetMapping("/")
    public String redirectToBooks(){
        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String getBook(@PathVariable("id") Long id,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageNumber, Model model){
        BookPageAttributes bookPageAttributes = pagesService.getBookPageAttributes(id, pageNumber);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("book", bookPageAttributes.getBook());
        model.addAttribute("comments", bookPageAttributes.getComments());
        model.addAttribute("page", bookPageAttributes.getPagingAttributes());
        model.addAttribute("email", user.getEmail());
        return "book";
    }

    @GetMapping("/books")
    public String getBooksView(@RequestParam(name = "page", required = false, defaultValue = "1") Integer pageNumber,
                               Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BooksPageAttributes booksPageAttributes = pagesService.getBooksPageAttributes(pageNumber);
        model.addAttribute("bookList", booksPageAttributes.getBookList());
        model.addAttribute("page", booksPageAttributes.getPagingAttributes());
        model.addAttribute("email", user.getEmail());
        return "books";
    }

    @GetMapping("/manage")
    public String getManageView(@RequestParam(name = "page", required = false, defaultValue = "1") Integer pageNumber, Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BooksPageAttributes booksPageAttributes = pagesService.getBooksPageAttributes(pageNumber);
        model.addAttribute("bookList", booksPageAttributes.getBookList());
        model.addAttribute("page", booksPageAttributes.getPagingAttributes());
        model.addAttribute("managePage", pageNumber);
        model.addAttribute("email", user.getEmail());
        return "manage";
    }

    @GetMapping("/book/update/{id}")
    public String getUpdateBookView(Model model, @PathVariable("id") long id){
        UpdateBookPageAttributes updateBookPageAttributes = pagesService.getUpdateBookPageAttributes(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("authors", updateBookPageAttributes.getAuthors());
        model.addAttribute("genres", updateBookPageAttributes.getGenres());
        model.addAttribute("book", updateBookPageAttributes.getBook());
        model.addAttribute("email", user.getEmail());
        return "update";
    }

    @GetMapping("/book/create")
    public String getCreateBookView(Model model){
        UpdateBookPageAttributes updateBookPageAttributes = pagesService.getCreateBookPageAttributes();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("authors", updateBookPageAttributes.getAuthors());
        model.addAttribute("genres", updateBookPageAttributes.getGenres());
        model.addAttribute("email", user.getEmail());
        return "create";
    }

}
