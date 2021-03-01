package org.course.service;

import lombok.AllArgsConstructor;
import org.course.configurations.AppConfig;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.dto.attributes.BookPageAttributes;
import org.course.dto.state.BookPageParams;
import org.course.exceptions.EntityNotFoundException;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.service.interfaces.BookPageService;
import org.course.utility.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookPageServiceImpl implements BookPageService {

    private final AppConfig appConfig;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public BookPageAttributes getBookPageAttributes(long bookId, int nextPage, BookPageParams previousParams) {
        Book book = bookRepository.findByIdEager(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found!"));
        List<Comment> commentList;
        BookPageParams pageParams;
        if (previousParams == null){
            Page<Comment> commentsPage = commentRepository.findByBook(book, PageRequest.of(0, appConfig.getCommentPageCount()));
            commentList = commentsPage.toList();
            pageParams = new BookPageParams( 0, commentsPage.getTotalPages());
        } else {
            int actualNextPage = PageUtils.getNextPageNumber(nextPage, previousParams.getTotalPages());
            Page<Comment> commentsPage = commentRepository.findByBook(book, PageRequest.of(actualNextPage, appConfig.getCommentPageCount()));
            commentList = commentsPage.toList();
            pageParams = new BookPageParams( actualNextPage, commentsPage.getTotalPages());
        }
        return new BookPageAttributes(book, commentList, pageParams);
    }



}
