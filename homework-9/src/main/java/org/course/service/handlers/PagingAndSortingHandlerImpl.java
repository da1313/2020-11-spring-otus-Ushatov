package org.course.service.handlers;

import org.course.service.interfaces.handlers.PagingAndSortingHandler;
import org.course.utility.BookSort;
import org.course.utility.PageUtils;
import org.course.utility.Route;
import org.springframework.stereotype.Service;

@Service
public class PagingAndSortingHandlerImpl implements PagingAndSortingHandler {

    private static final int FIRST_PAGE = 0;

    private static final BookSort DEFAULT_SORT = BookSort.NEW;

    @Override
    public int processPageNumber(Route route, int totalPages, int currentPage){

        return route != null ?
                PageUtils.getPage(route, totalPages, currentPage) :
                FIRST_PAGE;
    }

    @Override
    public BookSort processSort(Route route, BookSort currentSort, BookSort requestedSort){

        if (route != null){
            return currentSort;
        }

        return requestedSort == null ? DEFAULT_SORT : requestedSort;

    }

}
