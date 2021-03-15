package org.course.service.interfaces.handlers;

import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.utility.MainPageBehavior;

public interface MainPageHandler {

    MainPageAttributes getMainPageAttributes(MainPageRequest request, MainPageParams previousParams);

    MainPageBehavior getBehaviour();

}
