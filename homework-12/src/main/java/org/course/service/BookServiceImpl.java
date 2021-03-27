package org.course.service;

import lombok.AllArgsConstructor;
import org.course.api.request.BookRequest;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.exception.EntityNotFoundException;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public void deleteBook(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found!"));
        bookRepository.delete(book);
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
    public void createBook(BookRequest request) {
        Set<Genre> genreList = request.getGenreIds() == null ? new HashSet<>() : genreRepository.findByIds(request.getGenreIds());
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + request.getAuthorId() + " not found!"));
        Book book = Book.of(request.getTitle(), request.getDescription(), author, genreList);
        bookRepository.save(book);
    }

}
