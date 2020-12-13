package org.course.homework;

import org.course.homework.config.TestProperties;
import org.course.homework.dao.QuestionDaoLocale;
import org.course.homework.domain.Question;
import org.course.homework.service.interfaces.TestService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties(TestProperties.class)
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        context.getBean(TestService.class).run();
    }
}
