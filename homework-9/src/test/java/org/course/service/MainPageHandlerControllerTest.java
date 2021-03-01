package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.dto.request.MainPageRequest;
import org.course.dto.state.MainPageParams;
import org.course.service.interfaces.MainPageHandler;
import org.course.utility.BookSort;
import org.course.utility.MainPageBehavior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;


@DisplayName("Class MainPageHandlerController")
@ExtendWith(MockitoExtension.class)
class MainPageHandlerControllerTest {

    @Mock
    private MainPageHandlerByAll mainPageHandlerByAll;

    @Mock
    private MainPageHandlerByGenre mainPageHandlerByGenre;

    @Mock
    private MainPageHandlerByQuery mainPageHandlerByQuery;

    @Mock
    private Map<MainPageBehavior, MainPageHandler> handlerMap;

    @Test
    void shouldSetDefaultRequestIfRequestActionIsNull() {
        //Create service
        MainPageHandler handler = new MainPageHandlerController(handlerMap);

        //Set up expected
        MainPageRequest expectedRequest = new MainPageRequest(MainPageBehavior.BY_ALL, null, null, BookSort.POPULAR, null);

        //Set up input values
        MainPageRequest request = new MainPageRequest();//action == null
        MainPageParams params = new MainPageParams();

        //Mocking
        Mockito.when(handlerMap.get(MainPageBehavior.BY_ALL)).thenReturn(mainPageHandlerByAll);
        ArgumentCaptor<MainPageRequest> requestArgumentCaptor = ArgumentCaptor.forClass(MainPageRequest.class);
        ArgumentCaptor<MainPageParams> pageParamsArgumentCaptor = ArgumentCaptor.forClass(MainPageParams.class);

        //Run service
        handler.getMainPageAttributes(request, params);

        //Assertions
        Mockito.verify(mainPageHandlerByAll).getMainPageAttributes(requestArgumentCaptor.capture(), pageParamsArgumentCaptor.capture());
        Assertions.assertThat(requestArgumentCaptor.getValue()).isEqualTo(expectedRequest);

    }

    @Test
    void shouldSetDefaultParametersWhenSessionAttributesIsNull() {
        //Create service
        MainPageHandler handler = new MainPageHandlerController(handlerMap);

        //Set up expected
        MainPageParams expectedParams = new MainPageParams(MainPageBehavior.BY_ALL, null, null, 0,0,null);

        //Set up input values
        MainPageRequest request = new MainPageRequest(MainPageBehavior.BY_ALL, null, null, BookSort.NEW, null);
        MainPageParams params = null;

        //Mocking
        Mockito.when(handlerMap.get(MainPageBehavior.BY_ALL)).thenReturn(mainPageHandlerByAll);
        ArgumentCaptor<MainPageRequest> requestArgumentCaptor = ArgumentCaptor.forClass(MainPageRequest.class);
        ArgumentCaptor<MainPageParams> pageParamsArgumentCaptor = ArgumentCaptor.forClass(MainPageParams.class);

        //Run service
        handler.getMainPageAttributes(request, params);

        //Assertions
        Mockito.verify(mainPageHandlerByAll).getMainPageAttributes(requestArgumentCaptor.capture(), pageParamsArgumentCaptor.capture());
        Assertions.assertThat(pageParamsArgumentCaptor.getValue()).isEqualTo(expectedParams);

    }

}