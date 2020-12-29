package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.dao.intefaces.GenreDao;
import org.course.domain.Genre;
import org.course.service.intefaces.GenreHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreHandlerServiceImpl implements GenreHandlerService {

    private final GenreDao genreDao;

    @Override
    public String createGenre(String name) {
        Genre genre = new Genre(name);
        genreDao.create(genre);
        return "The genre is created!";
    }

    @Override
    public String readGenre(long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Genre genre = genreDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name");
        table.addRule();
        table.addRow(genre.getId(), genre.getName());
        table.addRule();
        return table.render();
    }

    @Override
    public String readAllGenres() {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        List<Genre> genres = genreDao.findAll();
        table.addRule();
        table.addRow("id", "name");
        table.addRule();
        for (Genre genre : genres) {
            table.addRow(genre.getId(), genre.getName());
            table.addRule();
        }
        return table.render();
    }

    @Override
    public String updateGenre(long id, String name) {
        Genre genre = genreDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
        genre.setName(name);
        genreDao.update(genre);
        return "The genre is updated!";
    }

    @Override
    public String deleteGenre(long id) {
        genreDao.deleteById(id);
        return "The genre is deleted!";
    }

    @Override
    public String deleteAllGenres() {
        genreDao.delete();
        return "All genres are deleted!";
    }

    @Override
    public String countGenres() {
        return String.valueOf(genreDao.count());
    }
}
