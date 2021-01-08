package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.dao.interfaces.GenreDao;
import org.course.domain.Genre;
import org.course.service.intefaces.GenreHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreHandlerServiceImpl implements GenreHandlerService {

    private final GenreDao genreDao;

    @Transactional
    @Override
    public String createGenre(String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genreDao.save(genre);
        return "The genre is created!";
    }

    @Transactional(readOnly = true)
    @Override
    public String readGenre(long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Genre genre = genreDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name", "books");
        table.addRule();
        table.addRow(genre.getId(), genre.getName(), genre.getBooks());
        table.addRule();
        return table.render();
    }

    @Transactional(readOnly = true)
    @Override
    public String readAllGenres() {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        List<Genre> genres = genreDao.findAll();
        table.addRule();
        table.addRow("id", "name", "books");
        table.addRule();
        for (Genre genre : genres) {
            table.addRow(genre.getId(), genre.getName(), genre.getBooks());
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateGenre(long id, String name) {
        Genre genre = genreDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        genre.setName(name);
        genreDao.save(genre);
        return "The genre is updated!";
    }

    @Transactional
    @Override
    public String deleteGenre(long id) {
        Genre genre = genreDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        genreDao.delete(genre);
        return "The genre is deleted!";
    }

    @Transactional
    @Override
    public String deleteAllGenres() {
        genreDao.deleteAll();
        return "All genres are deleted!";
    }

    @Transactional(readOnly = true)
    @Override
    public String countGenres() {
        return String.valueOf(genreDao.count());
    }
}
