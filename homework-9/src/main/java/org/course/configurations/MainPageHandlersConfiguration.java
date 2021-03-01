package org.course.configurations;

import lombok.AllArgsConstructor;
import org.course.components.interfaces.BookByGenreRepoResolver;
import org.course.components.interfaces.BookRepoResolver;
import org.course.repository.BookRepository;
import org.course.repository.GenreRepository;
import org.course.service.MainPageHandlerByAll;
import org.course.service.MainPageHandlerByGenre;
import org.course.service.MainPageHandlerByQuery;
import org.course.service.interfaces.MainPageHandler;
import org.course.service.interfaces.PagingAndSortingHandler;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class MainPageHandlersConfiguration {

    private final AppConfig appConfig;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final PagingAndSortingHandler pagingAndSortingHandler;

    private final Map<BookSort, BookRepoResolver> bookRepoResolverMap;

    private final Map<BookSort, BookByGenreRepoResolver> bookByGenreRepoResolverMap;

    @Bean
    public MainPageHandlerByAll getMainPageHandlerByAll(){
        return new MainPageHandlerByAll(pagingAndSortingHandler, genreRepository, bookRepoResolverMap);
    }

    @Bean
    public MainPageHandlerByGenre getMainPageHandlerByGenre(){
        return new MainPageHandlerByGenre(pagingAndSortingHandler, genreRepository, bookByGenreRepoResolverMap);
    }

    @Bean
    public MainPageHandlerByQuery getMainPageHandlerByQuery(){
        return new MainPageHandlerByQuery(pagingAndSortingHandler, appConfig, genreRepository, bookRepository);
    }

    @Bean
    public Map<MainPageBehavior, MainPageHandler> getMainPageHandlers(){
        Map<MainPageBehavior, MainPageHandler> bean = new HashMap<>();
        bean.put(MainPageBehavior.BY_ALL, getMainPageHandlerByAll());
        bean.put(MainPageBehavior.BY_GENRE, getMainPageHandlerByGenre());
        bean.put(MainPageBehavior.SEARCH, getMainPageHandlerByQuery());
        return bean;
    }

}
