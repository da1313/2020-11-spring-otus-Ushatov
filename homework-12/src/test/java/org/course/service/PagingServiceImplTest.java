package org.course.service;

import org.assertj.core.api.Assertions;
import org.course.api.attributes.PagingAttributes;
import org.course.config.AppConfig;
import org.course.service.interfaces.PagingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@DisplayName("Class PagingServiceImpl")
@ExtendWith(MockitoExtension.class)
class PagingServiceImplTest {

    public static final int LEFT = 1;
    public static final int RIGHT = 1;
    public static final int PAGE_NUMBER_1 = 1;
    public static final int PAGE_NUMBER_2 = 0;
    public static final int PAGE_NUMBER_3 = 2;
    public static final int TOTAL_PAGES = 3;
    public static final PagingAttributes CASE_1 = new PagingAttributes(TOTAL_PAGES, List.of(1), List.of(3),
            2, 3, 1);
    public static final PagingAttributes CASE_2 = new PagingAttributes(TOTAL_PAGES, List.of(), List.of(2,3),
            1, 2, 1);
    public static final PagingAttributes CASE_3 = new PagingAttributes(TOTAL_PAGES, List.of(1,2), List.of(),
            3, 3, 2);

    @Mock
    private AppConfig appConfig;

    @Test
    void shouldGetPageAttributesMiddleCase() {

        PagingService service = new PagingServiceImpl(appConfig);

        Mockito.when(appConfig.getLeftOffset()).thenReturn(LEFT);
        Mockito.when(appConfig.getRightOffset()).thenReturn(RIGHT);

        PagingAttributes actual = service.getPageAttributes(PAGE_NUMBER_1, TOTAL_PAGES);

        Assertions.assertThat(actual).isEqualTo(CASE_1);

    }

    @Test
    void shouldGetPageAttributesLeftCase() {

        PagingService service = new PagingServiceImpl(appConfig);

        Mockito.when(appConfig.getLeftOffset()).thenReturn(LEFT);
        Mockito.when(appConfig.getRightOffset()).thenReturn(RIGHT);

        PagingAttributes actual = service.getPageAttributes(PAGE_NUMBER_2, TOTAL_PAGES);

        Assertions.assertThat(actual).isEqualTo(CASE_2);

    }

    @Test
    void shouldGetPageAttributesRightCase() {

        PagingService service = new PagingServiceImpl(appConfig);

        Mockito.when(appConfig.getLeftOffset()).thenReturn(LEFT);
        Mockito.when(appConfig.getRightOffset()).thenReturn(RIGHT);

        PagingAttributes actual = service.getPageAttributes(PAGE_NUMBER_3, TOTAL_PAGES);

        Assertions.assertThat(actual).isEqualTo(CASE_3);

    }

}