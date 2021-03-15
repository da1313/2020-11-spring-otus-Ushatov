package org.course.service;

import org.course.service.handlers.MainPageHandlerByAll;
import org.course.service.handlers.MainPageHandlerByGenre;
import org.course.service.handlers.MainPageHandlerByQuery;
import org.course.service.interfaces.handlers.MainPageHandler;
import org.course.utility.MainPageBehavior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;


@DisplayName("Class MainPageHandlerController")
@ExtendWith(MockitoExtension.class)
class MainPageHandlerControllerTest {

    @Mock
    private MainPageHandlerByAll mainPageHandlerByAll;

    @Mock
    private MainPageHandlerByGenre mainPageHandlerByGenre;

    @Mock
    private MainPageHandlerByQuery mainPageHandlerByQuery;

    private final List<MainPageHandler> handlerList = new ArrayList<>();

    @BeforeEach
    void initHandlers(){
        handlerList.clear();

        Mockito.when(mainPageHandlerByAll.getBehaviour()).thenReturn(MainPageBehavior.BY_ALL);
        Mockito.when(mainPageHandlerByGenre.getBehaviour()).thenReturn(MainPageBehavior.BY_GENRE);
        Mockito.when(mainPageHandlerByQuery.getBehaviour()).thenReturn(null);

        handlerList.add(mainPageHandlerByAll);
        handlerList.add(mainPageHandlerByGenre);
        handlerList.add(mainPageHandlerByQuery);
    }

//    @Test
//    void shouldSetDefaultRequestIfRequestActionIsNull() {
//        //Create service
//        MainPageHandler handler = new MainPageHandlerController(handlerList);
//
//        //Set up expected
//        MainPageRequest expectedRequest = new MainPageRequest(MainPageBehavior.BY_ALL, null, null, BookSort.POPULAR, null);
//
//        //Set up input values
//        MainPageRequest request = new MainPageRequest();//action == null
//        MainPageParams params = new MainPageParams();
//
//        //Mocking
//        ArgumentCaptor<MainPageRequest> requestArgumentCaptor = ArgumentCaptor.forClass(MainPageRequest.class);
//        ArgumentCaptor<MainPageParams> pageParamsArgumentCaptor = ArgumentCaptor.forClass(MainPageParams.class);
//
//        //Run service
//        handler.getMainPageAttributes(request, params);
//
//        //Assertions
//        Mockito.verify(mainPageHandlerByAll).getMainPageAttributes(requestArgumentCaptor.capture(), pageParamsArgumentCaptor.capture());
//        Assertions.assertThat(requestArgumentCaptor.getValue()).isEqualTo(expectedRequest);
//
//    }
//
//    @Test
//    void shouldSetDefaultParametersWhenSessionAttributesIsNull() {
//        //Create service
//        MainPageHandler handler = new MainPageHandlerController(handlerList);
//
//        //Set up expected
//        MainPageParams expectedParams = new MainPageParams(MainPageBehavior.BY_ALL, null, null, 0,0,null);
//
//        //Set up input values
//        MainPageRequest request = new MainPageRequest(MainPageBehavior.BY_ALL, null, null, BookSort.NEW, null);
//        MainPageParams params = null;
//
//        //Mocking
//        ArgumentCaptor<MainPageRequest> requestArgumentCaptor = ArgumentCaptor.forClass(MainPageRequest.class);
//        ArgumentCaptor<MainPageParams> pageParamsArgumentCaptor = ArgumentCaptor.forClass(MainPageParams.class);
//
//        //Run service
//        handler.getMainPageAttributes(request, params);
//
//        //Assertions
//        Mockito.verify(mainPageHandlerByAll).getMainPageAttributes(requestArgumentCaptor.capture(), pageParamsArgumentCaptor.capture());
//        Assertions.assertThat(pageParamsArgumentCaptor.getValue()).isEqualTo(expectedParams);
//
//    }

}