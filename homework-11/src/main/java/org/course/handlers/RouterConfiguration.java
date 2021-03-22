package org.course.handlers;

import lombok.AllArgsConstructor;
import org.course.handlers.interfaces.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import java.util.Objects;

@Configuration
@AllArgsConstructor
public class RouterConfiguration {

    private final BookHandler bookHandler;

    private final CommentHandler commentHandler;

    private final AuthorHandler authorHandler;

    private final GenreHandler genreHandler;

    private final ImageHandler imageHandler;

    @Bean
    public RouterFunction<ServerResponse> getRoute(){
        return RouterFunctions.route()
                .GET("/books", RequestPredicates.queryParam("query", Objects::nonNull), bookHandler::getBooksByQuery)
                .GET("/books", RequestPredicates.queryParam("genreId", Objects::nonNull), bookHandler::getBooksByGenre)
                .GET("/books", bookHandler::getBooks)
                .GET("/books/{id}", bookHandler::getBookById)
                .POST("/books", bookHandler::createBook)
                .PUT("/books/{id}", bookHandler::updateBook)
                .DELETE("/books/{id}", bookHandler::deleteBook)
                .GET("/comments", commentHandler::getComments)
                .POST("/comments", commentHandler::createComment)
                .GET("/authors", authorHandler::getAuthors)
                .GET("/genres", genreHandler::getGenres)
                .GET("/image", imageHandler::getImage)
                .build();
    }

}
