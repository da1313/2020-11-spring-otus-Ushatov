package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.Genre;
import org.course.repository.AuthorRepository;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.GenreRepository;
import org.course.service.intefaces.BookHandlerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookHandlerServiceImpl implements BookHandlerService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public String createBook(String name, String authorId, String genreId) {

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found!"));

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));

        Book book = Book.of(name, author, List.of(genre));

        bookRepository.save(book);

        return "The book is created!";

    }

    @Override
    public String readBook(String id, int page, int size){

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));

        List<Comment> commentList = commentRepository.findByBook(book, PageRequest.of(page, size, Sort.by("time").descending()));

        AsciiTable bookTable = new AsciiTable();

        CWC_LongestLine cwc = new CWC_LongestLine();

        bookTable.getRenderer().setCWC(cwc);

        bookTable.addRule();

        bookTable.addRow("id", "name", "author", "genres", "comments count", "1", "2", "3", "4", "5", "avg. score");

        bookTable.addRule();

        bookTable.addRow(book.getId(),
                book.getTitle(),
                format(book.getAuthor()),
                book.getGenres(),
                book.getInfo().getCount(),
                book.getInfo().getOne(),
                book.getInfo().getTwo(),
                book.getInfo().getThree(),
                book.getInfo().getFour(),
                book.getInfo().getFive(),
                book.getInfo().getAvg());

        bookTable.addRule();

        AsciiTable commentsTable = new AsciiTable();

        commentsTable.getRenderer().setCWC(cwc);

        commentsTable.addRule();

        commentsTable.addRow("id", "user", "text", "time");

        commentsTable.addRule();

        for (Comment  comment : commentList) {
            commentsTable.addRow(comment.getId(), comment.getUser().getName(), comment.getText(), comment.getTime());
            commentsTable.addRule();
        }

        return bookTable.render() + "\n" + commentsTable.render();
    }


    @Transactional(readOnly = true)
    @Override
    public String readAllBooks(int page, int size) {

        AsciiTable table = new AsciiTable();

        CWC_LongestLine cwc = new CWC_LongestLine();

        table.getRenderer().setCWC(cwc);

        List<Book> books = bookRepository.findAll(PageRequest.of(page, size)).toList();

        table.addRule();

        table.addRow("id", "name", "author", "genres", "avg. score");

        table.addRule();

        for (Book book : books) {

            table.addRow(book.getId(),
                    book.getTitle(),
                    format(book.getAuthor()),
                    book.getGenres(),
                    book.getInfo().getAvg());
            table.addRule();

        }

        return table.render();

    }

    @Transactional
    @Override
    public String updateBook(String id, String name) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));

        book.setTitle(name);

        bookRepository.save(book);

        return "The book is updated!";

    }

    @Transactional
    @Override
    public String deleteBook(String id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));

        bookRepository.delete(book);

        return "The book is deleted!";

    }

    @Transactional
    @Override
    public String deleteAllBooks() {

        bookRepository.deleteAll();

        return "All books are deleted!";

    }

    @Transactional(readOnly = true)
    @Override
    public String countBooks() {
        return String.valueOf(bookRepository.count());
    }

    @Transactional
    @Override
    public String addGenre(String bookId, String genreId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
        bookRepository.addGenre(book, genre);
        return "The genre is added!";
    }

    @Transactional
    @Override
    public String removeGenre(String bookId, String genreId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
        bookRepository.removeGenre(book, genre);
        return "The genre is deleted!";
    }

    private Object format(Object value){
        return value == null ? "null" : value;
    }
}
