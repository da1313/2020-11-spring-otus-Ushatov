package org.course.service.interfaces;

import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;

public interface MainPageService {

    MainPageAttributes getMainPageAttributes(MainPageRequest request, MainPageParams previousParams);

}
