package org.course.service;

import lombok.AllArgsConstructor;
import org.course.configurations.AppConfig;
import org.course.domain.*;
import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.attributes.CreateBookPageAttributes;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.attributes.UpdateBookPageAttributes;
import org.course.dto.state.BookPageParams;
import org.course.dto.state.MainPageParams;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.BookHandleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BookHandleServiceImpl implements BookHandleService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final AppConfig appConfig;

    private final CommentRepository commentRepository;

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public MainPageAttributes getMainPageAttributes(long genreId, String sort, int nextPage, boolean isSearch, String query, MainPageParams previousParams){
        List<Genre> genreList = genreRepository.findAll();
        MainPageParams pageParams;
        Page<Book> bookPages;
        if (previousParams == null){
            bookPages = bookRepository.findAllEager(PageRequest.of(0, appConfig.getBookPageCount(), Sort.by("time").descending()));
            pageParams = new MainPageParams(0, "new", 0, bookPages.getTotalPages(), false, "");
        } else {
            if (isSearch){
                int actualNextPage = getNextPageNumber(nextPage, previousParams.getTotalPages());
                bookPages = bookRepository.findAllByQuery(query, PageRequest.of(actualNextPage, appConfig.getBookPageCount()));
                pageParams = new MainPageParams(0, "new", actualNextPage, bookPages.getTotalPages(), true, query);
            } else {
                if (genreId != 0){
                    Genre genre = genreRepository.findById(genreId).orElseThrow();
                    int actualNextPage = getNextPageNumber(nextPage, previousParams.getTotalPages());
                    bookPages = getNextBooksPageByGenre(actualNextPage, sort, genre);
                    pageParams = new MainPageParams(genreId, sort, actualNextPage, bookPages.getTotalPages(), false, "");
                } else {
                    int actualNextPage = getNextPageNumber(nextPage, previousParams.getTotalPages());
                    bookPages = getNextBooksPage(actualNextPage, sort);
                    pageParams = new MainPageParams(genreId, sort, actualNextPage, bookPages.getTotalPages(), false, "");
                }
            }
        }
        return new MainPageAttributes(pageParams, bookPages.toList(), genreList);
    }

    private int getNextPageNumber(int nextPage, int totalPages){
        if (totalPages == 0) return 0;
        if (nextPage < 0){
            return 0;
        } else if (nextPage >= totalPages){
            return totalPages - 1;
        } else {
            return nextPage;
        }
    }

    private Page<Book> getNextBooksPage(int nextPage, String sort){
        Page<Book> bookPages;
        switch (sort){
            case "new":
                bookPages = bookRepository.findAllEager(PageRequest.of(nextPage, appConfig.getBookPageCount(), Sort.by("time").descending()));
                break;
            case "best":
                bookPages = bookRepository.findAllSortedByAvgScore(PageRequest.of(nextPage, appConfig.getBookPageCount()));
                break;
            case "popular":
                bookPages = bookRepository.findAllEager(PageRequest.of(nextPage, appConfig.getBookPageCount(), Sort.by("bookInfo.commentCount").descending()));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return bookPages;
    }

    private Page<Book> getNextBooksPageByGenre(int nextPage, String sort, Genre genre){
        Page<Book> bookPages;
        switch (sort){
            case "new":
                bookPages = bookRepository.findAllByGenre(genre, PageRequest.of(nextPage, appConfig.getBookPageCount(), Sort.by("time").descending()));
                break;
            case "best":
                bookPages = bookRepository.findAllByGenreSortedByAvgScore(genre, PageRequest.of(nextPage, appConfig.getBookPageCount()));
                break;
            case "popular":
                bookPages = bookRepository.findAllByGenre(genre, PageRequest.of(nextPage, appConfig.getBookPageCount(), Sort.by("bookInfo.commentCount").descending()));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return bookPages;
    }

    @Transactional(readOnly = true)
    @Override
    public BookPageAttributes getBookPageAttributes(long bookId, int nextPage, BookPageParams previousParams) {
        Book book = bookRepository.findByIdEager(bookId).orElseThrow();
        List<Comment> commentList;
        BookPageParams pageParams;
        if (previousParams == null){
            Page<Comment> commentsPage = commentRepository.findByBook(book, PageRequest.of(0, appConfig.getCommentPageCount()));
            commentList = commentsPage.toList();
            pageParams = new BookPageParams(null, 0, commentsPage.getTotalPages());
        } else {
            int actualNextPage = getNextPageNumber(nextPage, previousParams.getTotalPages());
            Page<Comment> commentsPage = commentRepository.findByBook(book, PageRequest.of(actualNextPage, appConfig.getCommentPageCount()));
            commentList = commentsPage.toList();
            pageParams = new BookPageParams(null, actualNextPage, commentsPage.getTotalPages());
        }
        return new BookPageAttributes(book, commentList, pageParams);
    }

    @Transactional(readOnly = true)
    @Override
    public CreateBookPageAttributes getCreateBookPageAttributes() {
        List<Author> authorList = authorRepository.findAll();
        List<Genre> genreList = genreRepository.findAll();
        return new CreateBookPageAttributes(genreList, authorList);
    }

    @Transactional
    @Override
    public void createBook(String title, List<Long> genreIds, long authorId, String description) {
        Set<Genre> genreList = genreIds == null ? new HashSet<>() : genreRepository.findByIds(genreIds);
        Author author = authorRepository.findById(authorId).orElseThrow();
        Book book = new Book(0, title, description, author, null, new BookInfo(), genreList, null, null);
        bookRepository.save(book);
    }


    @Transactional(readOnly = true)
    @Override
    public UpdateBookPageAttributes getUpdateBookAttributes(long id) {
        Book book = bookRepository.findByIdEager(id).orElseThrow();
        List<Author> authorList = authorRepository.findAll();
        List<Genre> genreList = genreRepository.findAll();
        return new UpdateBookPageAttributes(genreList, authorList, book);
    }

    @Transactional
    @Override
    public void updateBook(long id, String title, List<Long> genreIds, long authorId, String description) {
        Book book = bookRepository.findByIdEager(id).orElseThrow();
        Author author = authorRepository.findById(authorId).orElseThrow();
        Set<Genre> genreList = genreIds == null ? new HashSet<>() : genreRepository.findByIds(genreIds);
        book.setTitle(title);
        book.setGenres(genreList);
        book.setAuthor(author);
        book.setDescription(description);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBook(long id) {
        Book book = bookRepository.findById(id).orElseThrow();
        bookRepository.delete(book);
    }
}
