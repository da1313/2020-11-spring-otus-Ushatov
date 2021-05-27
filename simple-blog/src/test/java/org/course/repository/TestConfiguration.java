package org.course.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public DataBeanImpl dataBean(){
        return new DataBeanImpl();
    }

}
