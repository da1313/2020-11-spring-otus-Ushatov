package org.course.service.handlers;

import org.course.components.interfaces.BookRepoResolver;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.repository.GenreRepository;
import org.course.service.interfaces.handlers.MainPageHandler;
import org.course.service.interfaces.handlers.PagingAndSortingHandler;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainPageHandlerByAll implements MainPageHandler {

    private final PagingAndSortingHandler pagingAndSortingHandler;

    private final GenreRepository genreRepository;

    private final Map<BookSort, BookRepoResolver> bookRepoResolverMap = new HashMap<>();

    private final List<BookRepoResolver> bookRepoResolverList;

    @Autowired
    public MainPageHandlerByAll(PagingAndSortingHandler pagingAndSortingHandler,
                                GenreRepository genreRepository, List<BookRepoResolver> bookRepoResolverList) {
        this.pagingAndSortingHandler = pagingAndSortingHandler;
        this.genreRepository = genreRepository;
        this.bookRepoResolverList = bookRepoResolverList;

        bookRepoResolverList.forEach(r -> bookRepoResolverMap.put(r.getSort(), r));

    }

    @Override
    public MainPageAttributes getMainPageAttributes(MainPageRequest request, MainPageParams previousParams) {

        List<Genre> genreList = genreRepository.findAll();

        int pageNumber = pagingAndSortingHandler
                .processPageNumber(request.getDirection(), previousParams.getTotalPages(), previousParams.getCurrentPage());

        BookSort sort = pagingAndSortingHandler.processSort(request.getDirection(), previousParams.getSort(), request.getSort());

        Page<Book> bookPage = bookRepoResolverMap.get(sort).getPage(pageNumber);

        MainPageParams params = new MainPageParams(request.getAction(), null,
                sort, pageNumber, bookPage.getTotalPages(), null);

        return new MainPageAttributes(params, bookPage.toList(), genreList);

    }

    @Override
    public MainPageBehavior getBehaviour() {
        return MainPageBehavior.BY_ALL;
    }

}
