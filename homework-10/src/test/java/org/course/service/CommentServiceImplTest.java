package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.api.pojo.CommentShort;
import org.course.api.requests.CommentListRequest;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.domain.*;
import org.course.repository.BookRepository;
import org.course.repository.CommentRepository;
import org.course.repository.UserRepository;
import org.course.service.interfaces.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DisplayName("Class CommentServiceImpl")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    public static final String BOOK_ID = "id";
    public static final int PAGE_SIZE = 1;
    public static final int PAGE_NUMBER = 0;
    public static final int TOTAL = 5;
    public static final List<Comment> COMMENT_LIST = List.of();
    public static final List<CommentShort> COMMENT_LIST_SHORT = List.of();
    public static final String TEXT = "text";
    public static final List<User> SOME_USER_LIST = List.of(new User("x", "y", "z", true));
    public static Book FAKE_BOOK;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void init(){

        FAKE_BOOK = new Book(BOOK_ID,
                "T",
                LocalDateTime.now(),
                new Author("id", "A"),
                List.of(new Genre("id", "G")),
                new Info());
    }

    @Test
    void shouldGetComments() {

        CommentService service = new CommentServiceImpl(commentRepository, bookRepository, userRepository);

        CommentListRequest request = new CommentListRequest(BOOK_ID, PAGE_SIZE, PAGE_NUMBER);

        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        PageImpl<Comment> page = new PageImpl<>(COMMENT_LIST,pageRequest, TOTAL);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(FAKE_BOOK));

        Mockito.when(commentRepository.findByBook(FAKE_BOOK, pageRequest)).thenReturn(page);

        CommentListResponse expected = new CommentListResponse(COMMENT_LIST_SHORT, TOTAL);

        CommentListResponse actual = service.getComments(request);

        Assertions.assertThat(actual).isEqualTo(expected);

    }

    @Test
    void shouldCreateComment() {

        CommentService service = new CommentServiceImpl(commentRepository, bookRepository, userRepository);

        CommentRequest request = new CommentRequest(BOOK_ID, TEXT);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(FAKE_BOOK));

        Mockito.when(userRepository.findAll()).thenReturn(SOME_USER_LIST);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        service.createComment(request);

        Mockito.verify(commentRepository).save(commentArgumentCaptor.capture());

        Comment expected = new Comment(null, TEXT, null, SOME_USER_LIST.get(0), FAKE_BOOK);

        Comment actual = commentArgumentCaptor.getValue();

        expected.setTime(actual.getTime());//!!

        Assertions.assertThat(actual).isEqualTo(expected);

    }
}