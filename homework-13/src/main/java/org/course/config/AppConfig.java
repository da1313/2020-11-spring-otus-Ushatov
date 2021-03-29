package org.course.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app")
@Data
@Configuration
public class AppConfig {

    private int bookPageSize;

    private int commentPageSize;

    private int leftOffset;

    private int rightOffset;

}
