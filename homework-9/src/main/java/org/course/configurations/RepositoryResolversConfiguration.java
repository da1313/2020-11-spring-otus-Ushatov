package org.course.configurations;

import lombok.AllArgsConstructor;
import org.course.components.*;
import org.course.components.interfaces.BookByGenreRepoResolver;
import org.course.components.interfaces.BookRepoResolver;
import org.course.repository.BookRepository;
import org.course.utility.BookSort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class RepositoryResolversConfiguration {

    private final AppConfig appConfig;

    private final BookRepository bookRepository;

    @Bean
    public BookRepoResolver getBookRepoResolverNew(){
        return new BookRepoResolverSortNew(appConfig, bookRepository);
    }

    @Bean
    public BookRepoResolver getBookRepoResolverBest(){
        return new BookRepoResolverSortBest(appConfig, bookRepository);
    }

    @Bean
    public BookRepoResolver getBookRepoResolverPopular(){
        return new BookRepoResolverSortPopular(appConfig, bookRepository);
    }

    @Bean
    public BookByGenreRepoResolver getBookByGenreRepoResolverNew(){
        return new BookByGenreRepoResolverSortNew(appConfig, bookRepository);
    }

    @Bean
    public BookByGenreRepoResolver getBookByGenreRepoResolverBest(){
        return new BookByGenreRepoResolverSortBest(appConfig, bookRepository);
    }

    @Bean
    public BookByGenreRepoResolver getBookByGenreRepoResolverPopular(){
        return new BookByGenreRepoResolverSortPopular(appConfig, bookRepository);
    }

    @Bean
    public Map<BookSort, BookRepoResolver> getBookPageResolvers(){
        Map<BookSort, BookRepoResolver> pageResolverMap = new HashMap<>();
        pageResolverMap.put(BookSort.NEW, getBookRepoResolverNew());
        pageResolverMap.put(BookSort.BEST, getBookRepoResolverBest());
        pageResolverMap.put(BookSort.POPULAR, getBookRepoResolverPopular());
        return pageResolverMap;
    }

    @Bean
    public Map<BookSort, BookByGenreRepoResolver> getBookByGenrePageResolvers(){
        Map<BookSort, BookByGenreRepoResolver> pageResolverMap = new HashMap<>();
        pageResolverMap.put(BookSort.NEW, getBookByGenreRepoResolverNew());
        pageResolverMap.put(BookSort.BEST, getBookByGenreRepoResolverBest());
        pageResolverMap.put(BookSort.POPULAR, getBookByGenreRepoResolverPopular());
        return pageResolverMap;
    }

}
