package org.course.exceptions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.course.api.responces.ResultResponse;
import org.course.changelog.TestDataInitializer;
import org.course.repository.BookRepository;
import org.course.testconfig.EmbeddedMongoConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration"})
@AutoConfigureMockMvc
@Import({EmbeddedMongoConfig.class, TestDataInitializer.class})
class AppExceptionHandlerTest {

    public static final ResultResponse RESULT_RESPONSE = new ResultResponse(false);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

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
    void shouldInterceptEntityNotFoundExceptionAndReturnSCNotFoundAndResultFalse() throws Exception {

        ObjectMapper mapper = getMapper();

        String response = mapper.writeValueAsString(RESULT_RESPONSE);

        Mockito.when(bookRepository.findById("1")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }
}