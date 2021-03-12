package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.Author;
import org.course.repository.AuthorRepository;
import org.course.service.interfaces.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }
}
