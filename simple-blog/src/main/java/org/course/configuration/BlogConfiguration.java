package org.course.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "blog")
@Configuration
@Data
public class BlogConfiguration {

    @Data
    static class Card{
        private double xScale;
        private double yScale;
    }

    private String accessKey;

    private String refreshKey;

}
