package org.course.controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.course.api.pojo.CommentShort;
import org.course.api.requests.CommentListRequest;
import org.course.api.requests.CommentRequest;
import org.course.api.responces.CommentListResponse;
import org.course.api.responces.ResultResponse;
import org.course.domain.*;
import org.course.service.interfaces.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class CommentController")
@WebMvcTest(CommentController.class)
@TestPropertySource(properties = "mongock.enabled=false")
class CommentControllerTest {

    private static final CommentListRequest COMMENT_LIST_REQUEST = new CommentListRequest("id", 0, 1);
    private static final CommentListResponse COMMENT_LIST_RESPONSE = new CommentListResponse(List.of(new CommentShort("z", "x", "z")), 5);
    private static final CommentRequest COMMENT_REQUEST = new CommentRequest("id", "text");
    private static final ResultResponse RESULT_RESPONSE = new ResultResponse(true);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

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
    void shouldInvokeGetComments() throws Exception {

        ObjectMapper mapper = getMapper();

        String responseJson = mapper.writeValueAsString(COMMENT_LIST_RESPONSE);

        Mockito.when(commentService.getComments(COMMENT_LIST_REQUEST)).thenReturn(COMMENT_LIST_RESPONSE);

        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                .param("id", COMMENT_LIST_REQUEST.getId())
                .param("pageSize", String.valueOf(COMMENT_LIST_REQUEST.getPageSize()))
                .param("pageNumber", String.valueOf(COMMENT_LIST_REQUEST.getPageNumber())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));

    }

    @Test
    void shouldInvokeCreateComment() throws Exception {

        ObjectMapper mapper = getMapper();

        String responseJson = mapper.writeValueAsString(RESULT_RESPONSE);

        String requestJson = mapper.writeValueAsString(COMMENT_REQUEST);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseJson));

        Mockito.verify(commentService).createComment(COMMENT_REQUEST);

    }
}