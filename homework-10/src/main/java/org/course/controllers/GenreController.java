package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.domain.Genre;
import org.course.service.interfaces.GenreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> getGenres(){
        return genreService.getGenres();
    }


}
