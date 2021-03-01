package org.course.service.interfaces;

import org.course.utility.BookSort;
import org.course.utility.Route;

public interface PagingAndSortingHandler {

    int processPageNumber(Route route, int totalPages, int currentPage);

    BookSort processSort(Route route, BookSort currentSort, BookSort requestedSort);

}
