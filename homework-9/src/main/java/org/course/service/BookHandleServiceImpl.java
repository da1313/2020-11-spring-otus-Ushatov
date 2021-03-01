package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.BookInfo;
import org.course.domain.Genre;
import org.course.dto.request.BookRequest;
import org.course.exceptions.EntityNotFoundException;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.BookHandleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class BookHandleServiceImpl implements BookHandleService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;


    @Transactional
    @Override
    public void createBook(BookRequest request) {
        Set<Genre> genreList = request.getGenreIds() == null ? new HashSet<>() : genreRepository.findByIds(request.getGenreIds());
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + request.getAuthorId() + " not found!"));
        Book book = new Book(0, request.getTitle(), request.getDescription(), author,
                null, new BookInfo(), genreList, null, null);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void updateBook(long id, BookRequest request) {
        Book book = bookRepository.findByIdEager(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found!"));
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + request.getAuthorId() + " not found!"));
        Set<Genre> genreList = request.getGenreIds() == null ? new HashSet<>() : genreRepository.findByIds(request.getGenreIds());
        book.setTitle(request.getTitle());
        book.setGenres(genreList);
        book.setAuthor(author);
        book.setDescription(request.getDescription());
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBook(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found!"));
        bookRepository.delete(book);
    }
}
