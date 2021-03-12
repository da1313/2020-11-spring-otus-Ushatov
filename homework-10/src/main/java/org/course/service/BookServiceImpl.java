package org.course.service;

import lombok.AllArgsConstructor;
import org.course.api.requests.BookListRequest;
import org.course.api.requests.BookRequest;
import org.course.api.responces.BookInfoResponse;
import org.course.api.responces.BookListResponse;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.Info;
import org.course.exceptions.EntityNotFoundException;
import org.course.api.pojo.BookShort;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.BookService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public BookListResponse getBooks(BookListRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPageNumber(), request.getPageSize(),
                Sort.by(request.getSort().getField()).descending());
        List<BookShort> bookList = bookRepository.findAllBookShortAuto(pageRequest);
        long count = bookRepository.count();
        int totalPages = (int) Math.ceil(((double) count) / request.getPageSize());
        return new BookListResponse(bookList, totalPages);
    }

    @Override
    public BookListResponse getBooksByGenre(BookListRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPageNumber(), request.getPageSize(),
                Sort.by(request.getSort().getField()).descending());
        List<BookShort> bookList = bookRepository.findAllBookShortByGenre(request.getGenreId(), pageRequest);
        long count = bookRepository.findCountByGenres(request.getGenreId()).getValue();
        int totalPages = (int) Math.ceil(((double) count) / request.getPageSize());
        return new BookListResponse(bookList, totalPages);
    }

    @Override
    public BookListResponse getBooksByQuery(BookListRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.by(request.getSort().getField()).descending());
        List<BookShort> bookList = bookRepository.findAllBookShortByQuery(request.getQuery(), pageRequest);
        long count = bookRepository.findCountByQuery(request.getQuery()).getValue();
        int totalPages = (int) Math.ceil(((double) count) / request.getPageSize());
        return new BookListResponse(bookList, totalPages);
    }

    @Override
    public BookInfoResponse getBookById(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        return new BookInfoResponse(book.getId(),
                book.getTitle(), book.getTime(), book.getAuthor(), book.getGenres(),
                getScoreCount(book.getInfo()), book.getInfo().getAvgScore(), book.getInfo().getCommentCount());
    }

    @Override
    public Book createBook(BookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + request.getAuthorId() + " not found"));
        List<Genre> genreList = genreRepository.findByIdIn(request.getGenresId());
        Book book = Book.of(request.getTitle(), author, genreList);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(String id, BookRequest request) {
        Book book = bookRepository.findById(id).orElseThrow();
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + request.getAuthorId() + " not found"));
        List<Genre> genreList = genreRepository.findByIdIn(request.getGenresId());
        book.setAuthor(author);
        book.setTitle(request.getTitle());
        book.setGenres(genreList);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        bookRepository.delete(book);
    }

    private List<Integer> getScoreCount(Info info){
        List<Integer> result = new ArrayList<>();
        result.add(info.getScoreOneCount());
        result.add(info.getScoreTwoCount());
        result.add(info.getScoreThreeCount());
        result.add(info.getScoreFourCount());
        result.add(info.getScoreFiveCount());
        return result;
    }
}
