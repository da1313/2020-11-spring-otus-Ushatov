package org.course.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {

    private int bookPageCount;
    private int commentPageCount;

}
