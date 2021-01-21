package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.dao.interfaces.AuthorDao;
import org.course.dao.interfaces.BookDao;
import org.course.dao.interfaces.GenreDao;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.service.intefaces.BookHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookHandlerServiceImpl implements BookHandlerService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Transactional
    @Override
    public String createBook(String name, String authorId, String genreId) {
        try {
            Author author = authorId.equals("null") ? null : authorDao.findById(Long.parseLong(authorId))
                    .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found!"));
            Genre genre = genreId.equals("null") ? null : genreDao.findById(Long.parseLong(genreId))
                    .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
            Book book = new Book();
            book.setName(name);
            book.setAuthor(author);
            book.addGenre(genre);
            bookDao.save(book);
            return "The book is created!";
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Parse error! " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String readBook(long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name", "author", "genres", "comments");
        table.addRule();
        table.addRow(book.getId(), book.getName(), format(book.getAuthor()), book.getGenres(), book.getComments());
        table.addRule();
        return table.render();
    }

    @Transactional(readOnly = true)
    @Override
    public String readAllBooks() {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        List<Book> books = bookDao.findAll();
        table.addRule();
        table.addRow("id", "name", "author", "genres", "comments");
        table.addRule();
        for (Book book : books) {
            table.addRow(book.getId(),
                    book.getName(),
                    format(book.getAuthor()),
                    book.getGenres(),
                    book.getComments());
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateBook(long id, String name, long authorId, String authorName) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        Author author = authorDao.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found!"));
        author.setName(authorName);
        authorDao.save(author);
        book.setName(name);
        book.setAuthor(author);
        bookDao.save(book);
        return "The book is updated!";
    }

    @Transactional
    @Override
    public String deleteBook(long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        bookDao.delete(book);
        return "The book is deleted!";
    }

    @Transactional
    @Override
    public String deleteAllBooks() {
        bookDao.deleteAll();
        return "All books are deleted!";
    }

    @Transactional(readOnly = true)
    @Override
    public String countBooks() {
        return String.valueOf(bookDao.count());
    }

    @Transactional
    @Override
    public String addGenre(long bookId, long genreId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Genre genre = genreDao.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
        book.addGenre(genre);
        return "The genre is added!";
    }

    @Transactional
    @Override
    public String removeGenre(long bookId, long genreId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Genre genre = genreDao.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
        book.removeGenre(genre);
        return "The genre is added!";
    }

    private Object format(Object value){
        return value == null ? "null" : value;
    }
}
