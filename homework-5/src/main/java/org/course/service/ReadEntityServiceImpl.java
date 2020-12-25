package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.dao.intefaces.AuthorDao;
import org.course.dao.intefaces.BookDao;
import org.course.dao.intefaces.CategoryDao;
import org.course.dao.intefaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Category;
import org.course.domain.Genre;
import org.course.service.intefaces.ReadEntityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReadEntityServiceImpl implements ReadEntityService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryDao categoryDao;

    @Override
    public String readEntityById(String tableName, long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        switch (tableName) {
            case "authors":
                Author author = authorDao.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found!"));
                table.addRule();
                table.addRow("id", "name");
                table.addRule();
                table.addRow(author.getId(), author.getName());
                table.addRule();
                return table.render();
            case "books":
                Book book = bookDao.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
                table.addRule();
                table.addRow("id", "name", "author", "genre", "categories");
                table.addRule();
                table.addRow(book.getId(), book.getName(), book.getAuthor(), book.getGenre(), book.getCategories());
                table.addRule();
                return table.render();
            case "genres":
                Genre genre = genreDao.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Genre with id " + id + " not found!"));
                table.addRule();
                table.addRow("id", "name");
                table.addRule();
                table.addRow(genre.getId(), genre.getName());
                table.addRule();
                return table.render();
            case "categories":
                Category category = categoryDao.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Category with id " + id + " not found!"));
                table.addRule();
                table.addRow("id", "name", "books");
                table.addRule();
                List<String> collect = category.getBooks()
                        .stream().map(b -> "Book(id=" + b.getId() + ", " + "name=" + b.getName() + ")")
                        .collect(Collectors.toList());
                table.addRow(category.getId(), category.getName(), collect);
                table.addRule();
                return table.render();
        }
        return null;
    }

    @Override
    public String readEntities(String tableName) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        switch (tableName) {
            case "authors":
                List<Author> authors = authorDao.findAll();
                table.addRule();
                table.addRow("id", "name");
                table.addRule();
                for (Author author : authors) {
                    table.addRow(author.getId(), author.getName());
                    table.addRule();
                }
                return table.render();
            case "books":
                List<Book> books = bookDao.findAll();
                table.addRule();
                table.addRow("id", "name", "author", "genre", "categories");
                table.addRule();
                for (Book book : books) {
                    table.addRow(book.getId(),
                            book.getName(),
                            format(book.getAuthor()),
                            format(book.getGenre()),
                            format(book.getCategories()));
                    table.addRule();
                }
                return table.render();
            case "genres":
                List<Genre> genres = genreDao.findAll();
                table.addRule();
                table.addRow("id", "name");
                table.addRule();
                for (Genre genre : genres) {
                    table.addRow(genre.getId(), genre.getName());
                    table.addRule();
                }
                return table.render();
            case "categories":
                List<Category> categories = categoryDao.findAll();
                table.addRule();
                table.addRow("id", "name", "books");
                table.addRule();
                for (Category category : categories) {
                    List<String> collect = category.getBooks()
                            .stream().map(b -> "Book(id=" + b.getId() + ", " + "name=" + b.getName() + ")")
                            .collect(Collectors.toList());
                    table.addRow(category.getId(), category.getName(), collect);
                    table.addRule();
                }
                return table.render();
        }
        return null;
    }

    private Object format(Object value){
        return value == null ? "null" : value;
    }

}
