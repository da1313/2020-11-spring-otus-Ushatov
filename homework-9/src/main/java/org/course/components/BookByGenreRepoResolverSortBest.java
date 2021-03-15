package org.course.components;

import lombok.AllArgsConstructor;
import org.course.components.interfaces.BookByGenreRepoResolver;
import org.course.configurations.AppConfig;
import org.course.domain.Book;
import org.course.domain.Genre;
import org.course.repository.BookRepository;
import org.course.utility.BookSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookByGenreRepoResolverSortBest implements BookByGenreRepoResolver {

    private final AppConfig appConfig;

    private final BookRepository bookRepository;

    @Override
    public Page<Book> getPage(Genre genre, int pageNumber) {
        return bookRepository.findAllByGenreSortedByAvgScore(genre, PageRequest.of(pageNumber, appConfig.getBookPageCount()));
    }

    @Override
    public BookSort getSort() {
        return BookSort.BEST;
    }
}
