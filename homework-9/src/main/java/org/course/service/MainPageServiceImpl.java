package org.course.service;

import org.course.dto.attributes.MainPageAttributes;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.service.interfaces.handlers.MainPageHandler;
import org.course.service.interfaces.MainPageService;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MainPageServiceImpl implements MainPageService {

    private final Map<MainPageBehavior, MainPageHandler> handlerMap = new HashMap<>();

    private final List<MainPageHandler> handlerList;

    @Autowired
    public MainPageServiceImpl(List<MainPageHandler> handlerList) {
        this.handlerList = handlerList;

        handlerList.stream().filter(r -> r.getBehaviour() != null)
                .forEach(r -> handlerMap.put(r.getBehaviour(), r));
    }

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
