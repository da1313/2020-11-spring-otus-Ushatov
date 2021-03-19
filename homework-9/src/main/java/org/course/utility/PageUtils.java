package org.course.utility;

public class PageUtils {

    public static int getNextPageNumber(int nextPage, int totalPages){
        if (totalPages == 0) return 0;
        if (nextPage < 0){
            return 0;
        } else if (nextPage >= totalPages){
            return totalPages - 1;
        } else {
            return nextPage;
        }
    }

    public static int getPage(Route route, int totalPages, int currentPage){
        if (route == null) return currentPage;
        if (route == Route.NEXT){
            return  (currentPage + 1) == totalPages ? currentPage : currentPage + 1;
        } else {
            return  (currentPage - 1) < 0 ? currentPage : currentPage - 1;
        }
    }

}
