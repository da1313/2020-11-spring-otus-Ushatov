package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.Genre;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }
}
