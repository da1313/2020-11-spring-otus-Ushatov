package org.course.contollers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.course.domain.Author;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.domain.User;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HypermediaController {

    private final RepositoryEntityLinks repositoryEntityLinks;

    @GetMapping("/hypermedia")
    public Object getResources(){

        return EntityModel.of(new Version("1"))
                .add(repositoryEntityLinks.linkFor(Author.class).withRel("authors"))
                .add(repositoryEntityLinks.linkFor(Genre.class).withRel("genres"))
                .add(repositoryEntityLinks.linkFor(User.class).withRel("users"))
                .add(repositoryEntityLinks.linkFor(Book.class).withRel("books"))
                .add(WebMvcLinkBuilder.linkTo(BookController.class).slash("api").slash("books").withRel("books_taken"));
    }

    @Data
    @AllArgsConstructor
    public static class Version {
        String version;
    }

}
