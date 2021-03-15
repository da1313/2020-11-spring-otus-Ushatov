package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.service.handlers.PagingAndSortingHandlerImpl;
import org.course.service.interfaces.handlers.PagingAndSortingHandler;
import org.course.utility.Route;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Class PagingAndSortingHandlerImpl")
class PagingAndSortingHandlerImplTest {

    public static final Route EMPTY_ROUTE = null;
    public static final int TOTAL_PAGES = 7;
    public static final int CURRENT_PAGE = 3;
    public static final int FIRST_PAGE = 0;
    public static final Route ROUTE = Route.NEXT;

    @Test
    void shouldReturnFirstPageWhenRequestIsNotPaging() {

        PagingAndSortingHandler handler = new PagingAndSortingHandlerImpl();

        int actual = handler.processPageNumber(EMPTY_ROUTE, TOTAL_PAGES, CURRENT_PAGE);

        Assertions.assertThat(actual).isEqualTo(FIRST_PAGE);

    }

    @Test
    void shouldReturnNextPageWhenRequestIsPaging() {

        PagingAndSortingHandler handler = new PagingAndSortingHandlerImpl();

        int actual = handler.processPageNumber(ROUTE, TOTAL_PAGES, CURRENT_PAGE);

        Assertions.assertThat(actual).isEqualTo(CURRENT_PAGE + 1);

    }

    @Test
    void shouldPreventCurrentPageToBeGreaterWhenTotal() {

        PagingAndSortingHandler handler = new PagingAndSortingHandlerImpl();

        int actual = handler.processPageNumber(Route.NEXT, TOTAL_PAGES, TOTAL_PAGES - 1);

        Assertions.assertThat(actual).isEqualTo(TOTAL_PAGES - 1);

    }

    @Test
    void shouldPreventCurrentPageToBeLessZero() {

        PagingAndSortingHandler handler = new PagingAndSortingHandlerImpl();

        int actual = handler.processPageNumber(Route.PREVIOUS, TOTAL_PAGES, 0);

        Assertions.assertThat(actual).isEqualTo(0);

    }

}