package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.dao.intefaces.AuthorDao;
import org.course.domain.Author;
import org.course.service.intefaces.AuthorHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorHandlerServiceImpl implements AuthorHandlerService {

    private final AuthorDao authorDao;

    @Override
    public String createAuthor(String name) {
        Author author = new Author(name);
        authorDao.create(author);
        return "The author is created!";
    }

    @Override
    public String readAuthor(long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Author author = authorDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name");
        table.addRule();
        table.addRow(author.getId(), author.getName());
        table.addRule();
        return table.render();
    }

    @Override
    public String readAllAuthors() {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        List<Author> authors = authorDao.findAll();
        table.addRule();
        table.addRow("id", "name");
        table.addRule();
        for (Author author : authors) {
            table.addRow(author.getId(), author.getName());
            table.addRule();
        }
        return table.render();
    }

    @Override
    public String updateAuthor(long id, String name) {
        Author author = authorDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found!"));
        author.setName(name);
        authorDao.update(author);
        return "The author is updated!";
    }

    @Override
    public String deleteAuthor(long id) {
        authorDao.deleteById(id);
        return "The author is deleted!";
    }

    @Override
    public String deleteAllAuthors() {
        authorDao.delete();
        return "All authors are deleted!";
    }

    @Override
    public String countAuthors() {
        return String.valueOf(authorDao.count());
    }
}
