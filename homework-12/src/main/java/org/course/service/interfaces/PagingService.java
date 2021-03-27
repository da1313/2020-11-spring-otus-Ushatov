package org.course.service.interfaces;

import org.course.api.attributes.PagingAttributes;

public interface PagingService {

    PagingAttributes getPageAttributes(int pageNumber, int totalPages);

}
