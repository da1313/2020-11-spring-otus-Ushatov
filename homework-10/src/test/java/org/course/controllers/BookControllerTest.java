package org.course.controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.assertj.core.api.Assertions;
import org.course.api.requests.BookListRequest;
import org.course.api.requests.BookRequest;
import org.course.api.responces.BookInfoResponse;
import org.course.api.responces.BookListResponse;
import org.course.api.responces.ResultResponse;
import org.course.domain.*;
import org.course.api.pojo.BookShort;
import org.course.service.interfaces.BookService;
import org.course.service.interfaces.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@DisplayName("Class BookController")
@WebMvcTest(BookController.class)
@TestPropertySource(properties = "mongock.enabled=false")
class BookControllerTest {

    private final static String GENRE_ID = "1";
    private final static String QUERY = "1";
    private final static BookListRequest BOOK_LIST_REQUEST = new BookListRequest(0,1, BookSort.NEW);
    private final static BookListResponse BOOK_LIST_RESPONSE = new BookListResponse(List.of(new BookShort()), 5);
    private final static BookInfoResponse BOOK_INFO_RESPONSE = new BookInfoResponse("1","t", LocalDateTime.now(),new Author("1", "x"),
            List.of(new Genre("1", "y")),List.of(0,1,2,3,4),4.5,3);
    public static final String BOOK_ID_FROM_PATH = "1";
    private final static BookRequest BOOK_REQUEST = new BookRequest("t", "1", List.of("1"));
    private static final ResultResponse RESULT_RESPONSE = new ResultResponse(true);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private ImageService imageService;

    private ObjectMapper getMapper(){

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        });

        mapper.registerModule(module);

        return mapper;
    }

    @Test
    void shouldInvokeGetBooks() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String bookListResponseJson = mapper.writeValueAsString(BOOK_LIST_RESPONSE);

        Mockito.when(bookService.getBooks(BOOK_LIST_REQUEST, null, null)).thenReturn(BOOK_LIST_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .param("pageNumber", String.valueOf(BOOK_LIST_REQUEST.getPageNumber()))
                .param("pageSize", String.valueOf(BOOK_LIST_REQUEST.getPageSize()))
                .param("sort", BOOK_LIST_REQUEST.getSort().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(bookListResponseJson));

    }

    @Test
    void shouldInvokeGetBooksByGenre() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String bookListResponseJson = mapper.writeValueAsString(BOOK_LIST_RESPONSE);

        Mockito.when(bookService.getBooks(BOOK_LIST_REQUEST, GENRE_ID, null)).thenReturn(BOOK_LIST_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .param("pageNumber", String.valueOf(BOOK_LIST_REQUEST.getPageNumber()))
                .param("pageSize", String.valueOf(BOOK_LIST_REQUEST.getPageSize()))
                .param("sort", BOOK_LIST_REQUEST.getSort().toString())
                .param("genreId", GENRE_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(bookListResponseJson));

    }

    @Test
    void shouldInvokeGetBooksByQuery() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String bookListResponseJson = mapper.writeValueAsString(BOOK_LIST_RESPONSE);

        Mockito.when(bookService.getBooks(BOOK_LIST_REQUEST, null, QUERY)).thenReturn(BOOK_LIST_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                .param("pageNumber", String.valueOf(BOOK_LIST_REQUEST.getPageNumber()))
                .param("pageSize", String.valueOf(BOOK_LIST_REQUEST.getPageSize()))
                .param("sort", BOOK_LIST_REQUEST.getSort().toString())
                .param("query", QUERY))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(bookListResponseJson));

    }

    @Test
    void shouldInvokeGetBookById() throws Exception {

        ObjectMapper mapper = getMapper();

        String bookInfoResponse = mapper.writeValueAsString(BOOK_INFO_RESPONSE);

        Mockito.when(bookService.getBookById(BOOK_ID_FROM_PATH)).thenReturn(BOOK_INFO_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(bookInfoResponse));

    }

    @Test
    void shouldInvokeCreateBook() throws Exception {

        ObjectMapper mapper = getMapper();

        String requestJson = mapper.writeValueAsString(BOOK_REQUEST);

        String responseJson = mapper.writeValueAsString(RESULT_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));

        Mockito.verify(bookService).createBook(BOOK_REQUEST);

    }

    @Test
    void shouldInvokeUpdateBook() throws Exception {

        ObjectMapper mapper = getMapper();

        String requestJson = mapper.writeValueAsString(BOOK_REQUEST);

        String responseJson = mapper.writeValueAsString(RESULT_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.put("/books/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));

        Mockito.verify(bookService).updateBook(BOOK_ID_FROM_PATH, BOOK_REQUEST);

    }

    @Test
    void shouldInvokeDeleteBook() throws Exception {

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(bookService).deleteBook(stringArgumentCaptor.capture());

        String actual = stringArgumentCaptor.getValue();

        Assertions.assertThat(actual).isEqualTo(BOOK_ID_FROM_PATH);

    }
}