package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.domain.*;
import org.course.repository.AuthorRepository;
import org.course.repository.BookCommentRepository;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.repository.projections.BookStatistics;
import org.course.service.intefaces.BookHandlerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookHandlerServiceImpl implements BookHandlerService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookCommentRepository bookCommentRepository;

    @Transactional
    @Override
    public String createBook(String name, String authorId, String genreId) {
        try {
            Author author = authorId.equals("null") ? null : authorRepository.findById(Long.parseLong(authorId))
                    .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found!"));
            Genre genre = genreId.equals("null") ? null : genreRepository.findById(Long.parseLong(genreId))
                    .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
            Book book = new Book();
            book.setName(name);
            book.setAuthor(author);
            book.addGenre(genre);
            bookRepository.save(book);
            return "The book is created!";
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Parse error! " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String readBook(long id, int page, int size) {
        AsciiTable bookTable = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        bookTable.getRenderer().setCWC(cwc);
        Book book = bookRepository.findWithAuthorAndGenreById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        List<BookComment> bookComments = bookCommentRepository
                .findByBook(book, PageRequest.of(page, size));
        BookStatistics bookStat = bookRepository.findBookStatistics(book)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        bookTable.addRule();
        bookTable.addRow("id", "name", "author", "genres", "comments count","1", "2", "3", "4", "5", "avg. score");
        bookTable.addRule();
        long scoreCount = bookStat.getOne() + bookStat.getTwo() + bookStat.getThree() + bookStat.getFour() + bookStat.getFive();
        double avgScore = scoreCount == 0 ? 0 :
                ((double) (bookStat.getOne() +
                2 * bookStat.getTwo() +
                3 * bookStat.getThree() +
                4 * bookStat.getFour() +
                5 * bookStat.getFive())) /  scoreCount;
        bookTable.addRow(book.getId(),
                book.getName(),
                format(book.getAuthor()),
                book.getGenres(),
                bookStat.getCommentCount(),
                bookStat.getOne(),
                bookStat.getTwo(),
                bookStat.getThree(),
                bookStat.getFour(),
                bookStat.getFive(),
                avgScore);
        bookTable.addRule();
        AsciiTable commentsTable = new AsciiTable();
        commentsTable.getRenderer().setCWC(cwc);
        commentsTable.addRule();
        commentsTable.addRow("id", "user", "text");
        commentsTable.addRule();
        for (BookComment  bookComment : bookComments) {
            commentsTable.addRow(bookComment.getId(), bookComment.getUser().getName(), bookComment.getText());
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
        List<Book> books = bookRepository.findAllWithAuthor(PageRequest.of(page, size));
        table.addRule();
        table.addRow("id", "name", "author", "genres", "avg. score");
        table.addRule();
        for (Book book : books) {
            int scoreCount = book.getBookScores().size();
            double averageScore = scoreCount == 0 ? 0 : ((double) book.getBookScores().stream()
                    .map(BookScore::getScore).reduce(Integer::sum).orElse(0)) / book.getBookScores().size();
            table.addRow(book.getId(),
                    book.getName(),
                    format(book.getAuthor()),
                    book.getGenres(),
                    averageScore);
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateBook(long id, String name, long authorId, String authorName) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + " not found!"));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + authorId + " not found!"));
        author.setName(authorName);
        authorRepository.save(author);
        book.setName(name);
        book.setAuthor(author);
        bookRepository.save(book);
        return "The book is updated!";
    }

    @Transactional
    @Override
    public String deleteBook(long id) {
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
    public String addGenre(long bookId, long genreId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
        book.addGenre(genre);
        return "The genre is added!";
    }

    @Transactional
    @Override
    public String removeGenre(long bookId, long genreId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalArgumentException("Genre with id " + genreId + " not found!"));
        book.removeGenre(genre);
        return "The genre is added!";
    }

    private Object format(Object value){
        return value == null ? "null" : value;
    }
}
