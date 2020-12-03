package org.course.homework;

import org.course.homework.service.TestServiceImpl;
import org.course.homework.service.interfaces.PrintService;
import org.course.homework.service.PrintServiceWithStream;
import org.springframework.context.annotation.*;

@ComponentScan
@Configuration
@PropertySource("application.properties")
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        TestServiceImpl app = context.getBean(TestServiceImpl.class);
        app.run();
        context.close();
    }

    @Bean
    public PrintService getPrintService(){
        return new PrintServiceWithStream(System.out, System.in);
    }
}
