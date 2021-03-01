package org.course.service.interfaces;

import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;

public interface MainPageHandler {

    MainPageAttributes getMainPageAttributes(MainPageRequest request, MainPageParams previousParams);

}
