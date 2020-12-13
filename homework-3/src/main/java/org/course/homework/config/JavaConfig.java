package org.course.homework.config;

import lombok.AllArgsConstructor;
import org.course.homework.service.PrintServiceWithStream;
import org.course.homework.service.interfaces.PrintService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@AllArgsConstructor
public class JavaConfig {

    private final TestProperties testProperties;

    @Bean
    public PrintService getPrintService(){
        return new PrintServiceWithStream(System.out, System.in);
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:" +
                testProperties.getLocaleLocation() +
                (testProperties.getLocaleLocation().equals("") || testProperties.getLocaleLocation().endsWith("/")? "" : "/" )+
                testProperties.getLocaleNamePrefix());
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
