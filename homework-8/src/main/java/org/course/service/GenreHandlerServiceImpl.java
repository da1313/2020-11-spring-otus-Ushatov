package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.domain.Genre;
import org.course.repository.GenreRepository;
import org.course.service.intefaces.GenreHandlerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreHandlerServiceImpl implements GenreHandlerService {

    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public String createGenre(String name) {
        Genre genre = Genre.of(name);
        genreRepository.save(genre);
        return "The genre is created!";
    }

    @Transactional(readOnly = true)
    @Override
    public String readGenre(String id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name");
        table.addRule();
        table.addRow(genre.getId(), genre.getName());
        table.addRule();
        return table.render();
    }

    @Transactional(readOnly = true)
    @Override
    public String readAllGenres(int page, int size) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        List<Genre> genres = genreRepository.findAll(PageRequest.of(page, size)).toList();
        table.addRule();
        table.addRow("id", "name");
        table.addRule();
        for (Genre genre : genres) {
            table.addRow(genre.getId(), genre.getName());
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateGenre(String id, String name) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        genre.setName(name);
        genreRepository.save(genre);
        return "The genre is updated!";
    }

    @Transactional
    @Override
    public String deleteGenre(String id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        genreRepository.delete(genre);
        return "The genre is deleted!";
    }

    @Transactional
    @Override
    public String deleteAllGenres() {
        genreRepository.deleteAll();
        return "All genres are deleted!";
    }

    @Transactional(readOnly = true)
    @Override
    public String countGenres() {
        return String.valueOf(genreRepository.count());
    }
}
