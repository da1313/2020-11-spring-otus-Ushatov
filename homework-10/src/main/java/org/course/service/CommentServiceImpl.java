package org.course.service;

import lombok.AllArgsConstructor;
import org.course.api.pojo.CommentShort;
import org.course.api.requests.CommentListRequest;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.User;
import org.course.exceptions.EntityNotFoundException;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.UserRepository;
import org.course.service.interfaces.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Override
    public CommentListResponse getComments(CommentListRequest request) {
        Book book = bookRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + request.getId() + " not found"));
        Page<Comment> commentPage = commentRepository.findByBook(book,
                PageRequest.of(request.getPageNumber(), request.getPageSize()));
        List<CommentShort> commentList = commentPage.toList().stream()
                .map(c -> new CommentShort(c.getId(), c.getText(), c.getUser().getName())).collect(Collectors.toList());
        int totalPages = commentPage.getTotalPages();
        return new CommentListResponse(commentList, totalPages);
    }

    @Override
    public Comment createComment(CommentRequest request) {
        Book book = bookRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + request.getId() + " not found"));
        User user = userRepository.findAll().get(0);//i know, i know . . .
        Comment comment = Comment.of(request.getText(), user, book);
        return commentRepository.save(comment);
    }
}
