package org.course.service;

import lombok.AllArgsConstructor;
import org.course.components.interfaces.BookByGenreRepoResolver;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.exceptions.EntityNotFoundException;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.MainPageHandler;
import org.course.service.interfaces.PagingAndSortingHandler;
import org.course.utility.BookSort;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
public class MainPageHandlerByGenre implements MainPageHandler {

    private final PagingAndSortingHandler pagingAndSortingHandler;

    private final GenreRepository genreRepository;

    private final Map<BookSort, BookByGenreRepoResolver> bookByGenreRepoResolverMap;

    @Override
    public MainPageAttributes getMainPageAttributes(MainPageRequest request, MainPageParams previousParams) {

        List<Genre> genreList = genreRepository.findAll();

        long genreId = request.getGenreId() == null ? previousParams.getGenreId() : request.getGenreId();

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + genreId + " not found!"));

        int pageNumber = pagingAndSortingHandler
                .processPageNumber(request.getDirection(), previousParams.getTotalPages(), previousParams.getCurrentPage());

        BookSort sort = pagingAndSortingHandler.processSort(request.getDirection(), previousParams.getSort(), request.getSort());

        Page<Book> bookPage = bookByGenreRepoResolverMap.get(sort).getPage(genre, pageNumber);

        MainPageParams params = new MainPageParams(request.getAction(), genreId, sort, pageNumber, bookPage.getTotalPages(), null);

        return new MainPageAttributes(params, bookPage.toList(), genreList);

    }
}
