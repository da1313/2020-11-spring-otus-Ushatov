package org.course.homework.config;

import org.course.homework.service.PrintServiceWithStream;
import org.course.homework.service.interfaces.PrintService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfig {

    @Bean
    public PrintService getPrintService(){
        return new PrintServiceWithStream(System.out, System.in);
    }

}
