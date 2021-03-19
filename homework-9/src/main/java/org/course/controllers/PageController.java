package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.attributes.CreateBookPageAttributes;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.attributes.UpdateBookPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.BookPageParams;
import org.course.dto.state.MainPageParams;
import org.course.service.MainPageServiceImpl;
import org.course.service.interfaces.BookPageService;
import org.course.service.interfaces.CreateBookPageService;
import org.course.service.interfaces.MainPageService;
import org.course.service.interfaces.UpdateBookPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes({PageController.MAIN_PAGE_PARAMS, PageController.BOOK_PAGE_PARAMS})
@Controller
@AllArgsConstructor
public class PageController {

    public static final String MAIN_PAGE_PARAMS = "mainPageParams";
    public static final String BOOK_PAGE_PARAMS = "bookPageParams";

    private final MainPageService mainPageService;

    private final BookPageService bookPageService;

    private final CreateBookPageService createBookPageService;

    private final UpdateBookPageService updateBookPageService;

    @GetMapping("/")
    public String getMainView(MainPageRequest request, Model model){
        MainPageParams previousParams = (MainPageParams) model.getAttribute(MAIN_PAGE_PARAMS);
        MainPageAttributes mainPageAttributes = mainPageService.getMainPageAttributes(request, previousParams);
        model.addAttribute("mainPageParams", mainPageAttributes.getPageParams());
        model.addAttribute("books", mainPageAttributes.getBookList());
        model.addAttribute("genres", mainPageAttributes.getGenreList());
        return "main";
    }

    @GetMapping("/{id}")
    public String getBookView(@PathVariable("id") long bookId,
                              @RequestParam(value = "nextPage", required = false, defaultValue = "0") int nextPage,
                              Model model){
        BookPageParams previousParams = (BookPageParams) model.getAttribute(BOOK_PAGE_PARAMS);
        BookPageAttributes bookPageAttributes = bookPageService.getBookPageAttributes(bookId, nextPage, previousParams);
        model.addAttribute("bookPageParams", bookPageAttributes.getBookPageParams());
        model.addAttribute("book", bookPageAttributes.getBook());
        model.addAttribute("comments", bookPageAttributes.getCommentList());
        return "book";
    }

    @GetMapping("/book/create")
    public String getCreateBookView(Model model){
        CreateBookPageAttributes createBookPageAttributes = createBookPageService.getCreateBookPageAttributes();
        model.addAttribute("authors", createBookPageAttributes.getAuthorList());
        model.addAttribute("genres", createBookPageAttributes.getGenreList());
        return "create";
    }

    @GetMapping("/book/update/{id}")
    public String getUpdateBookView(Model model, @PathVariable("id") long id){
        UpdateBookPageAttributes updateBookAttributes = updateBookPageService.getUpdateBookAttributes(id);
        model.addAttribute("authors", updateBookAttributes.getAuthorList());
        model.addAttribute("genres", updateBookAttributes.getGenreList());
        model.addAttribute("book", updateBookAttributes.getBook());
        return "update";
    }

}
