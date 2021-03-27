package org.course.service;

import lombok.AllArgsConstructor;
import org.course.api.attributes.BookPageAttributes;
import org.course.api.attributes.BooksPageAttributes;
import org.course.api.attributes.PagingAttributes;
import org.course.api.attributes.UpdateBookPageAttributes;
import org.course.config.AppConfig;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.Genre;
import org.course.exception.EntityNotFoundException;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.PagesService;
import org.course.service.interfaces.PagingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PagesServiceImpl implements PagesService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final PagingService pagingService;

    private final AppConfig appConfig;

    @Transactional(readOnly = true)
    @Override
    public BooksPageAttributes getBooksPageAttributes(int pageNumber) {
        int actualPageNumber = pageNumber < 1 ? 1 : pageNumber - 1;
        Page<Book> bookPage = bookRepository.findAllEager(PageRequest.of(actualPageNumber, appConfig.getBookPageSize(), Sort.by("id").ascending()));
        int totalPages = bookPage.getTotalPages();
        PagingAttributes pageAttributes = pagingService.getPageAttributes(actualPageNumber, totalPages);
        return new BooksPageAttributes(bookPage.toList(), pageAttributes);
    }

    @Transactional(readOnly = true)
    @Override
    public UpdateBookPageAttributes getUpdateBookPageAttributes(long id) {
        Book book = bookRepository.findByIdEager(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found!"));
        List<Author> authorList = authorRepository.findAll();
        List<Genre> genreList = genreRepository.findAll();
        return new UpdateBookPageAttributes(authorList, genreList, book);
    }

    @Transactional(readOnly = true)
    @Override
    public UpdateBookPageAttributes getCreateBookPageAttributes() {
        List<Author> authorList = authorRepository.findAll();
        List<Genre> genreList = genreRepository.findAll();
        return new UpdateBookPageAttributes(authorList, genreList, null);
    }

    @Transactional(readOnly = true)
    @Override
    public BookPageAttributes getBookPageAttributes(long id, int pageNumber) {
        int actualPageNumber = pageNumber < 1 ? 1 : pageNumber - 1;
        Book book = bookRepository.findByIdEager(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found!"));
        Page<Comment> commentPage = commentRepository.findByBook(book, PageRequest.of(actualPageNumber,
                appConfig.getCommentPageSize(), Sort.by("time").descending()));
        int totalPages = commentPage.getTotalPages();
        PagingAttributes pageAttributes = pagingService.getPageAttributes(actualPageNumber, totalPages);
        return new BookPageAttributes(book, commentPage.toList(), pageAttributes);
    }
}
