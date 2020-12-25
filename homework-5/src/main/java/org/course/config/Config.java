package org.course.config;

import org.course.service.PrintServiceWithStream;
import org.course.service.intefaces.PrintService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public PrintService printService(){
        return new PrintServiceWithStream(System.out, System.in);
    }

}
