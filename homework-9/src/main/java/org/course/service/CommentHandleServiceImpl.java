package org.course.service;

import lombok.AllArgsConstructor;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.User;
import org.course.exceptions.EntityNotFoundException;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.UserRepository;
import org.course.service.interfaces.CommentHandleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommentHandleServiceImpl implements CommentHandleService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createComment(long id, String text) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found!"));
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + 1L + " not found!"));
        Comment comment = new Comment(0, text, book, user);
        commentRepository.save(comment);
    }
}
