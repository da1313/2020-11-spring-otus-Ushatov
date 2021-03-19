package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.Author;
import org.course.domain.Genre;
import org.course.dto.attributes.CreateBookPageAttributes;
import org.course.repository.AuthorRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.CreateBookPageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateBookPageServiceImpl implements CreateBookPageService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public CreateBookPageAttributes getCreateBookPageAttributes() {
        List<Author> authorList = authorRepository.findAll();
        List<Genre> genreList = genreRepository.findAll();
        return new CreateBookPageAttributes(genreList, authorList);
    }

}
