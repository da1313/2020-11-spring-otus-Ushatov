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
import org.course.service.intefaces.BookHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookHandlerServiceImpl implements BookHandlerService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final CategoryDao categoryDao;

    @Override
    public String createBook(String name, String authorId, String genreId) {
        try {
            Author author = authorId.equals("null") ? null : authorDao.findById(Long.parseLong(authorId))
                    .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found!"));
            Genre genre = genreId.equals("null") ? null : genreDao.findById(Long.parseLong(genreId))
                    .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
            Book book = new Book(name, author, genre);
            bookDao.create(book);
            return "The book is created!";
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Parse error! " + e.getMessage());
        }
    }

    @Override
    public String readBook(long id) {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        table.addRule();
        table.addRow("id", "name", "author", "genre", "categories");
        table.addRule();
        table.addRow(book.getId(), book.getName(), book.getAuthor(), book.getGenre(), book.getCategories());
        table.addRule();
        return table.render();
    }

    @Override
    public String readAllBooks() {
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
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
    }

    @Override
    public String updateBook(long id, String name, long authorId, String authorName, long genreId, String genreName) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        Author author = authorDao.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found!"));
        Genre genre = genreDao.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
        author.setName(authorName);
        authorDao.update(author);
        genre.setName(genreName);
        genreDao.update(genre);
        book.setName(name);
        book.setAuthor(author);
        book.setGenre(genre);
        bookDao.update(book);
        return "The book is updated!";
    }

    @Override
    public String deleteBook(long id) {
        bookDao.deleteById(id);
        return "The book is deleted!";
    }

    @Override
    public String deleteAllBooks() {
        bookDao.delete();
        return "All books are deleted!";
    }

    @Override
    public String countBooks() {
        return String.valueOf(bookDao.count());
    }

    @Override
    public String addCategory(long bookId, long categoryId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + categoryId + " not found!"));
        bookDao.addCategory(book, category);
        return "The category added!";
    }

    @Override
    public String removeCategory(long bookId, long categoryId) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + categoryId + " not found!"));
        bookDao.removeCategory(book, category);
        return "The category removed!";
    }

    private Object format(Object value){
        return value == null ? "null" : value;
    }
}
