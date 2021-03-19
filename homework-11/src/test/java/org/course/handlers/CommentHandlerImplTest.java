package org.course.handlers;

import com.mongodb.client.result.UpdateResult;
import org.assertj.core.api.Assertions;
import org.course.api.pojo.BookCount;
import org.course.api.pojo.CommentShort;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.api.responces.ResultResponse;
import org.course.domain.Book;
import org.course.domain.Comment;
import org.course.domain.User;
import org.course.domain.embedded.Info;
import org.course.domain.embedded.UserEmbedded;
import org.course.repositories.BookRepository;
import org.course.repositories.CommentRepository;
import org.course.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import testdata.TestUpdateResult;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Class CommentHandlerImpl")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CommentHandlerImplTest {

    public static final long COUNT = 6L;
    public static final int PAGE = 0;
    public static final int SIZE = 1;
    public static final long TOTAL_PAGES = (int) Math.ceil(((double) COUNT) / SIZE);
    public static final String BOOK_ID = "1";
    public static final String TITLE = "T";
    public static final String COMMENT_TEXT = "T";
    public static final Comment COMMENT_1 = new Comment("1", "T1", null, new UserEmbedded("1", "N1"), null);
    public static final Comment COMMENT_2 = new Comment("2", "T2", null, new UserEmbedded("2", "N2"), null);
    public static final User USER = new User("1", "N", null, true);

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebTestClient client;

    @Test
    void shouldReturnCommentsResponse() {

        Book book = new Book(BOOK_ID, TITLE, null, null, null, new Info());

        List<Comment> commentList = List.of(COMMENT_1, COMMENT_2);

        List<CommentShort> commentShortList = commentList.stream().map(c -> new CommentShort(c.getId(), c.getText(), c.getUser().getName()))
                .collect(Collectors.toList());

        CommentListResponse expected = new CommentListResponse(commentShortList, TOTAL_PAGES);

        PageRequest pageRequest = PageRequest.of(PAGE, SIZE);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Mono.just(book));

        Mockito.when(commentRepository.findByBookId(BOOK_ID, pageRequest)).thenReturn(Flux.just(commentList.get(0), commentList.get(1)));

        Mockito.when(commentRepository.findCountByBook(BOOK_ID)).thenReturn(Mono.just(new BookCount(COUNT)));

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/comments")
                        .queryParam("id", BOOK_ID)
                        .queryParam("pageNumber", PAGE)
                        .queryParam("pageSize", SIZE).build())
                .exchange()
                .expectStatus().isOk().expectBody(CommentListResponse.class)
                .isEqualTo(expected);

    }

    @Test
    void shouldCreateCommentAndIncreaseCommentCountInBookReturnTrueOnSuccess() {

        Book book = new Book(BOOK_ID, TITLE, null, null, null, new Info());

        List<User> userList = List.of(USER);

        CommentRequest request = new CommentRequest(BOOK_ID, COMMENT_TEXT);

        ResultResponse response = new ResultResponse(true);

        Comment expected = Comment.of(COMMENT_TEXT, userList.get(0), book);

        Mockito.when(bookRepository.findById(BOOK_ID)).thenReturn(Mono.just(book));

        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(userList.get(0)));

        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(Mono.just(expected));

        Mockito.when(bookRepository.increaseCommentCount(BOOK_ID)).thenReturn(Mono.just(new TestUpdateResult(1)));

        client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/comments").build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk().expectBody(ResultResponse.class)
                .isEqualTo(response);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        Mockito.verify(commentRepository).save(commentArgumentCaptor.capture());

        Comment actual = commentArgumentCaptor.getValue();

        expected.setTime(actual.getTime());

        Assertions.assertThat(actual).isEqualTo(expected);

    }
}