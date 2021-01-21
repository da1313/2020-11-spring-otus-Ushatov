package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.dao.interfaces.BookCommentDao;
import org.course.dao.interfaces.BookDao;
import org.course.domain.Book;
import org.course.domain.BookComment;
import org.course.service.intefaces.BookCommentHandleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookCommentHandleServiceImpl implements BookCommentHandleService {

    private final BookCommentDao bookCommentDao;
    private final BookDao bookDao;

    @Transactional
    @Override
    public String createBookComment(long bookId, String text) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        BookComment bookComment = new BookComment();
        bookComment.setBook(book);
        bookComment.setText(text);
        bookCommentDao.save(bookComment);
        return "The comment is added!";
    }

    @Transactional(readOnly = true)
    @Override
    public String readBookComment(long id) {
        BookComment bookComment = bookCommentDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("id", "book", "text");
        table.addRule();
        table.addRow(bookComment.getId(), bookComment.getBook(), bookComment.getText());
        table.addRule();
        return table.render();
    }

    @Transactional(readOnly = true)
    @Override
    public String readAllBookComments() {
        List<BookComment> bookComments = bookCommentDao.findAll();
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("id", "book", "text");
        table.addRule();
        for (BookComment bookComment : bookComments) {
            table.addRow(bookComment.getId(), bookComment.getBook(), bookComment.getText());
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateBookComment(long id, String text) {
        BookComment bookComment = bookCommentDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        bookComment.setText(text);
        bookCommentDao.save(bookComment);
        return "The comment is updated!";
    }

    @Transactional
    @Override
    public String deleteBookComment(long id) {
        BookComment bookComment = bookCommentDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        bookCommentDao.delete(bookComment);
        return "The comment is deleted!";
    }

    @Transactional
    @Override
    public String deleteAllBookComments() {
        bookCommentDao.deleteAll();
        return "The comments is deleted!";
    }

    @Transactional(readOnly = true)
    @Override
    public String countBookComments() {
        return String.valueOf(bookCommentDao.count());
    }
}
