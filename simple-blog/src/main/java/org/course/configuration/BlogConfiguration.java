package org.course.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    private String domain;

    private String email;

    private List<String> moderationStatusList;

}
