package org.course.service;

import lombok.AllArgsConstructor;
import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.service.interfaces.MainPageHandler;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MainPageHandlerController implements MainPageHandler {

    private final Map<MainPageBehavior, MainPageHandler> handlerMap;

    @Override
    @Transactional(readOnly = true)
    public MainPageAttributes getMainPageAttributes(MainPageRequest request, MainPageParams previousParams){

        MainPageRequest actualRequest = request.getAction() == null ?
                new MainPageRequest(MainPageBehavior.BY_ALL, null, null, BookSort.POPULAR, null) :
                request;


        return handlerMap.get(actualRequest.getAction())
                .getMainPageAttributes(actualRequest, Objects.requireNonNullElseGet(previousParams,
                        () -> new MainPageParams(MainPageBehavior.BY_ALL, null, null, 0,0,null)));

    }

}
