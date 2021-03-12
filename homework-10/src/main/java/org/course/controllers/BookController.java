package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.api.requests.BookListRequest;
import org.course.api.requests.BookRequest;
import org.course.api.responces.BookInfoResponse;
import org.course.api.responces.BookListResponse;
import org.course.api.responces.ResultResponse;
import org.course.service.interfaces.BookService;
import org.course.service.interfaces.ImageService;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final ImageService imageService;

    @GetMapping("/books")
    public BookListResponse getBooks(BookListRequest request){

        return bookService.getBooks(request);

    }

    @GetMapping("/books/genre")
    public BookListResponse getBooksByGenre(BookListRequest request){

        return bookService.getBooksByGenre(request);

    }

    @GetMapping("/books/search")
    public BookListResponse getBooksByQuery(BookListRequest request){

        return bookService.getBooksByQuery(request);

    }

    @GetMapping("/books/{id}")
    public BookInfoResponse getBookById(@PathVariable String id){

        return bookService.getBookById(id);

    }

    @GetMapping("/books/image/download")//todo new contoller
    public byte[] getImage(){
        return imageService.getImage();
    }

    @PostMapping("/books")
    public ResultResponse createBook(@RequestBody BookRequest request){

        bookService.createBook(request);

        return new ResultResponse(true);

    }

    @PutMapping("/books/{id}")
    public ResultResponse updateBook(@PathVariable String id, @RequestBody BookRequest request){

        bookService.updateBook(id, request);

        return new ResultResponse(true);

    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable String id){

        bookService.deleteBook(id);

    }
}
