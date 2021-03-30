package org.course.service;

import lombok.AllArgsConstructor;
import org.course.api.attributes.PagingAttributes;
import org.course.config.AppConfig;
import org.course.service.interfaces.PagingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PagingServiceImpl implements PagingService {

    private final AppConfig appConfig;

    @Override
    public PagingAttributes getPageAttributes(int pageNumber, int totalPages) {
        int leftOffset = appConfig.getLeftOffset();
        int rightOffset = appConfig.getRightOffset();

        List<Integer> pagesBefore = new ArrayList<>();
        List<Integer> pagesAfter = new ArrayList<>();

        int nextPage = Math.min(pageNumber + 1, totalPages - 1) + 1;
        int previousPage = Math.max(pageNumber - 1, 0) + 1;

        int rightAdd = pageNumber - leftOffset < 0 ? leftOffset - pageNumber : 0;
        int leftAdd = pageNumber + rightOffset >= totalPages ? pageNumber + rightOffset - totalPages + 1 : 0;
        int realLeftOffset = Math.max((pageNumber - leftOffset - leftAdd), 0);
        int realRightOffset = Math.min((pageNumber + rightOffset + rightAdd), totalPages - 1);

        for (int i = realLeftOffset; i < pageNumber; i++) {
            pagesBefore.add(i + 1);
        }
        for (int i = pageNumber + 1; i <= realRightOffset; i++) {
            pagesAfter.add(i + 1);
        }

        return new PagingAttributes(totalPages, pagesBefore, pagesAfter, pageNumber + 1, nextPage, previousPage);
    }
}
