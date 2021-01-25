package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.domain.Book;
import org.course.domain.BookComment;
import org.course.domain.User;
import org.course.repository.BookCommentRepository;
import org.course.repository.BookRepository;
import org.course.service.intefaces.BookCommentHandleService;
import org.course.service.intefaces.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookCommentHandleServiceImpl implements BookCommentHandleService {

    private final BookCommentRepository bookCommentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public String createBookComment(long bookId, long userId, String text) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        BookComment bookComment = new BookComment();
        bookComment.setBook(book);
        bookComment.setUser(user);
        bookComment.setText(text);
        bookCommentRepository.save(bookComment);
        return "The comment is added!";
    }

    @Transactional(readOnly = true)
    @Override
    public String readBookComment(long id) {
        BookComment bookComment = bookCommentRepository.findWithBookAndUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("id", "book", "user", "text");
        table.addRule();
        table.addRow(bookComment.getId(), bookComment.getBook(), bookComment.getUser(), bookComment.getText());
        table.addRule();
        return table.render();
    }

    @Transactional(readOnly = true)
    @Override
    public String readAllBookComments(int page, int size) {
        List<BookComment> bookComments = bookCommentRepository.findAllWithBookAndUser(PageRequest.of(page, size));
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("id", "book", "user", "text");
        table.addRule();
        for (BookComment bookComment : bookComments) {
            table.addRow(bookComment.getId(), bookComment.getBook(), bookComment.getUser(), bookComment.getText());
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateBookComment(long id, String text) {
        BookComment bookComment = bookCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        bookComment.setText(text);
        bookCommentRepository.save(bookComment);
        return "The comment is updated!";
    }

    @Transactional
    @Override
    public String deleteBookComment(long id) {
        BookComment bookComment = bookCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        bookCommentRepository.delete(bookComment);
        return "The comment is deleted!";
    }

    @Transactional
    @Override
    public String deleteAllBookComments() {
        bookCommentRepository.deleteAll();
        return "The comments is deleted!";
    }

    @Transactional(readOnly = true)
    @Override
    public String countBookComments() {
        return String.valueOf(bookCommentRepository.count());
    }
}
