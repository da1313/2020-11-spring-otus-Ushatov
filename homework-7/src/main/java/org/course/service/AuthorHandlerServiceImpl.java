package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.domain.Author;
import org.course.repository.AuthorRepository;
import org.course.service.intefaces.AuthorHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorHandlerServiceImpl implements AuthorHandlerService {

    private final AuthorRepository authorRepository;

    @Transactional
    @Override
    public String createAuthor(String name) {
        Author author = new Author();
        author.setName(name);
        authorRepository.save(author);
        return "The author is created!";
    }

    @Transactional(readOnly = true)
    @Override
    public String readAuthor(long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name", "books");
        table.addRule();
        table.addRow(author.getId(), author.getName(), author.getBooks());
        table.addRule();
        return table.render();
    }

    @Transactional(readOnly = true)
    @Override
    public String readAllAuthors() {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        List<Author> authors = authorRepository.findAll();
        table.addRule();
        table.addRow("id", "name", "books");
        table.addRule();
        for (Author author : authors) {
            table.addRow(author.getId(), author.getName(), author.getBooks());
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateAuthor(long id, String name) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found!"));
        author.setName(name);
        authorRepository.save(author);
        return "The author is updated!";
    }

    @Transactional
    @Override
    public String deleteAuthor(long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found!"));
        authorRepository.delete(author);
        return "The author is deleted!";
    }

    @Transactional
    @Override
    public String deleteAllAuthors() {
        authorRepository.deleteAll();
        return "All authors are deleted!";
    }

    @Transactional(readOnly = true)
    @Override
    public String countAuthors() {
        return String.valueOf(authorRepository.count());
    }
}
