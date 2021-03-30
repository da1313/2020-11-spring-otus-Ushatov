package org.course.service;

import lombok.AllArgsConstructor;
import org.course.api.request.CommentRequest;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.User;
import org.course.exception.EntityNotFoundException;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.service.interfaces.CommentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional
    @Override
    public void createComment(CommentRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + request.getBookId() + " not found!"));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comment comment = new Comment(0, request.getText(), LocalDateTime.now(), book, user);

        commentRepository.save(comment);

        book.getInfo().setCommentCount(book.getInfo().getCommentCount() + 1);

        bookRepository.save(book);

    }
}
