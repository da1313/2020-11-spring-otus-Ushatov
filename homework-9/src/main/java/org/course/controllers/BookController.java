package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.attributes.CreateBookPageAttributes;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.attributes.UpdateBookPageAttributes;
import org.course.dto.state.BookPageParams;
import org.course.dto.state.MainPageParams;
import org.course.service.interfaces.BookHandleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SessionAttributes({"mainPageParams", "bookPageParams"})
@Controller
@AllArgsConstructor
public class BookController {

    private final BookHandleService bookHandleService;

    @GetMapping("/")
    public String getMainView(@RequestParam(name = "genreId", required = false, defaultValue = "0") long genreId,
                           @RequestParam(name = "sort", required = false, defaultValue = "new") String sort,
                           @RequestParam(name = "nextPage", required = false, defaultValue = "0") int nextPage,
                           @RequestParam(name = "isSearch", required = false, defaultValue = "false") boolean isSearch,
                           @RequestParam(name = "query", required = false, defaultValue = "") String query,
                           Model model){
        MainPageParams previousParams = (MainPageParams) model.getAttribute("mainPageParams");
        MainPageAttributes mainPageAttributes = bookHandleService.getMainPageAttributes(genreId, sort, nextPage, isSearch, query, previousParams);
        model.addAttribute("mainPageParams", mainPageAttributes.getPageParams());
        model.addAttribute("books", mainPageAttributes.getBookList());
        model.addAttribute("genres", mainPageAttributes.getGenreList());
        return "main";
    }



    @GetMapping("/{id}")
    public String getBookView(@PathVariable("id") long bookId,
                              @RequestParam(value = "nextPage", required = false, defaultValue = "0") int nextPage,
                              Model model){
        BookPageParams previousParams = (BookPageParams) model.getAttribute("bookPageParams");
        BookPageAttributes bookPageAttributes = bookHandleService.getBookPageAttributes(bookId, nextPage, previousParams);
        model.addAttribute("bookPageParams", bookPageAttributes.getBookPageParams());
        model.addAttribute("book", bookPageAttributes.getBook());
        model.addAttribute("comments", bookPageAttributes.getCommentList());
        return "book";
    }

    @GetMapping("/book/create")
    public String getCreateBookView(Model model){
        CreateBookPageAttributes createBookPageAttributes = bookHandleService.getCreateBookPageAttributes();
        model.addAttribute("authors", createBookPageAttributes.getAuthorList());
        model.addAttribute("genres", createBookPageAttributes.getGenreList());
        return "create";
    }

    @PostMapping("/book/create")
    public String createBook(@RequestParam("title") String title,
                          @RequestParam("genreList") List<Long> genreIds,
                          @RequestParam("authorId") long authorId,
                             @RequestParam("description") String description){
        bookHandleService.createBook(title, genreIds, authorId, description);
        return "redirect:/";
    }

    @GetMapping("/book/update/{id}")
    public String getUpdateBookView(Model model, @PathVariable("id") long id){
        UpdateBookPageAttributes updateBookAttributes = bookHandleService.getUpdateBookAttributes(id);
        model.addAttribute("authors", updateBookAttributes.getAuthorList());
        model.addAttribute("genres", updateBookAttributes.getGenreList());
        model.addAttribute("book", updateBookAttributes.getBook());
        return "update";
    }

    @PostMapping("/book/update/{id}")
    public String updateBook(@PathVariable("id") long id,
                             @RequestParam("title") String title,
                          @RequestParam("genreList") List<Long> genreIds,
                          @RequestParam("authorId") long authorId,
                             @RequestParam("description") String description){
        bookHandleService.updateBook(id, title, genreIds, authorId, description);
        return "redirect:/";
    }

    @GetMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable("id") long id){
        bookHandleService.deleteBook(id);
        return "redirect:/";
    }

}
