package org.course.homework;

import lombok.RequiredArgsConstructor;
import org.course.homework.domain.User;
import org.course.homework.service.interfaces.LoginService;
import org.course.homework.service.interfaces.SaveResultService;
import org.course.homework.service.interfaces.StatisticService;
import org.course.homework.service.interfaces.TestService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;


@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {

    private final TestService testService;
    private final SaveResultService saveResultService;
    private final LoginService loginService;
    private final StatisticService statisticService;


    @ShellMethod(value = "Run test with specified parameters", key = {"test", "t"})
    @ShellMethodAvailability("isCommandAvailable")
    public void test(){
        User user = loginService.getUser();
        double rate = testService.run(user.getName());
        saveResultService.saveResult(rate, user);
    }

    @ShellMethod(value = "Print current user test statistic", key = {"stat", "s"})
    @ShellMethodAvailability("isCommandAvailable")
    public String printStatistic(){
        User user = loginService.getUser();
        return statisticService.getStatistics(user);
    }

    @ShellMethod(value = "Authenticate user", key = {"login", "l"})
    public String login(@ShellOption(defaultValue = "anonymous") String username){
        loginService.login(username);
        return "You are logged in as " + username;
    }

    private Availability isCommandAvailable(){
        return loginService.checkUser() ? Availability.available() : Availability.unavailable("you should login first!!");
    }

}
