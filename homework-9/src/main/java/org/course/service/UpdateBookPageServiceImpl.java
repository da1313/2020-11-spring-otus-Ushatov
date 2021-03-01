package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.dto.attributes.UpdateBookPageAttributes;
import org.course.exceptions.EntityNotFoundException;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.UpdateBookPageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UpdateBookPageServiceImpl implements UpdateBookPageService {

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public UpdateBookPageAttributes getUpdateBookAttributes(long id) {
        Book book = bookRepository.findByIdEager(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found!"));
        List<Author> authorList = authorRepository.findAll();
        List<Genre> genreList = genreRepository.findAll();
        return new UpdateBookPageAttributes(genreList, authorList, book);
    }

}
