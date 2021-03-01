package org.course.service;

import lombok.AllArgsConstructor;
import org.course.configurations.AppConfig;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.MainPageHandler;
import org.course.service.interfaces.PagingAndSortingHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;


@AllArgsConstructor
public class MainPageHandlerByQuery implements MainPageHandler {

    private final PagingAndSortingHandler pagingAndSortingHandler;

    private final AppConfig appConfig;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public MainPageAttributes getMainPageAttributes(MainPageRequest request, MainPageParams previousParams) {

        List<Genre> genreList = genreRepository.findAll();

        String query = request.getQuery() == null ? previousParams.getQuery() : request.getQuery();

        int pageNumber = pagingAndSortingHandler
                .processPageNumber(request.getDirection(), previousParams.getTotalPages(), previousParams.getCurrentPage());

        Page<Book> bookPage = bookRepository.findAllByQuery(query,
                PageRequest.of(pageNumber, appConfig.getBookPageCount()));

        MainPageParams params = new MainPageParams(request.getAction(),
                null, null, pageNumber, bookPage.getTotalPages(), query);

        return new MainPageAttributes(params, bookPage.toList(), genreList);

    }
}
