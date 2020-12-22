package org.course.homework;

import org.course.homework.domain.User;
import org.course.homework.service.interfaces.LoginService;
import org.course.homework.service.interfaces.SaveResultService;
import org.course.homework.service.interfaces.StatisticService;
import org.course.homework.service.interfaces.TestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Class ApplicationCommands")
@ExtendWith(MockitoExtension.class)
class ApplicationCommandsTest {

    @Mock
    private TestService testService;
    @Mock
    private SaveResultService saveResultService;
    @Mock
    private LoginService loginService;
    @Mock
    private StatisticService statisticService;

    public final static int USER_ID = 0;
    public final static String USER_NAME = "Ivan";
    public final static double USER_RATE = 100.0;

    @DisplayName("Gри активации метода проводится тест и сохраняются результаты")
    @Test
    void shouldInvokeTestAndSaveResults() {
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        Mockito.when(loginService.getUser()).thenReturn(user);
        Mockito.when(testService.run(USER_NAME)).thenReturn(USER_RATE);
        ApplicationCommands applicationCommands = new ApplicationCommands(
                testService,
                saveResultService,
                loginService,
                statisticService);
        applicationCommands.test();
        assertAll(
                () -> Mockito.verify(testService, Mockito.times(1)).run(USER_NAME),
                () -> Mockito.verify(saveResultService, Mockito.times(1)).saveResult(USER_RATE, user)
        );
    }

    @DisplayName("При активации метода выводится статистика")
    @Test
    void shouldInvokeStatisticsServiceAndReturnItOutPut() {
        User user = new User();
        user.setId(USER_ID);
        user.setName(USER_NAME);
        Mockito.when(loginService.getUser()).thenReturn(user);
        Mockito.when(statisticService.getStatistics(user)).thenReturn("doStuff");
        ApplicationCommands applicationCommands = new ApplicationCommands(
                testService,
                saveResultService,
                loginService,
                statisticService);
        String output = applicationCommands.printStatistic();

        assertAll(
                () -> Mockito.verify(statisticService, Mockito.times(1)).getStatistics(user),
                () -> assertEquals("doStuff", output)
        );
    }

    @Test
    void shouldInvokeLoginServiceAndReturnMessage() {
        ApplicationCommands applicationCommands = new ApplicationCommands(
                testService,
                saveResultService,
                loginService,
                statisticService);
        String output = applicationCommands.login(USER_NAME);
        Mockito.verify(loginService, Mockito.times(1)).login(USER_NAME);
        assertEquals("You are logged in as " + USER_NAME, output);
    }
}