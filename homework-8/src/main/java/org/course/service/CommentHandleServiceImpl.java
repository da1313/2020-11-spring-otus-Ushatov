package org.course.service;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import lombok.AllArgsConstructor;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.User;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.UserRepository;
import org.course.service.intefaces.CommentHandleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentHandleServiceImpl implements CommentHandleService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public String createBookComment(String bookId, String userId, String text) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + " not found!"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found!"));
        Comment comment = Comment.of(text, user, book);
        commentRepository.save(comment);
        return "The comment is added!";
    }

    @Transactional(readOnly = true)
    @Override
    public String readBookComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("id", "book", "user", "text");
        table.addRule();
        table.addRow(comment.getId(), comment.getBook(), comment.getUser(), comment.getText());
        table.addRule();
        return table.render();
    }

    @Transactional(readOnly = true)
    @Override
    public String readAllBookComments(int page, int size) {
        List<Comment> bookComments = commentRepository.findAll(PageRequest.of(page, size)).toList();
        AsciiTable table = new AsciiTable();
        CWC_LongestLine cwc = new CWC_LongestLine();
        table.getRenderer().setCWC(cwc);
        table.addRule();
        table.addRow("id", "book", "user", "text");
        table.addRule();
        for (Comment bookComment : bookComments) {
            table.addRow(bookComment.getId(), bookComment.getBook(), bookComment.getUser(), bookComment.getText());
            table.addRule();
        }
        return table.render();
    }

    @Transactional
    @Override
    public String updateBookComment(String id, String text) {
        Comment bookComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        bookComment.setText(text);
        commentRepository.save(bookComment);
        return "The comment is updated!";
    }

    @Transactional
    @Override
    public String deleteBookComment(String id) {
        Comment bookComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with id " + id + " not found!"));
        commentRepository.delete(bookComment);
        return "The comment is deleted!";
    }

    @Transactional
    @Override
    public String deleteAllBookComments() {
        commentRepository.deleteAll();
        return "The comments is deleted!";
    }

    @Transactional(readOnly = true)
    @Override
    public String countBookComments() {
        return String.valueOf(commentRepository.count());
    }
}
