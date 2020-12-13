package org.course.homework.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Locale;

@ConfigurationProperties(prefix = "test")
@ConstructorBinding
@Getter
@AllArgsConstructor
public class TestProperties {

    private final Locale locale;
    private final String csvNamePrefix;
    private final String csvLocation;
    private final String localeNamePrefix;
    private final String localeLocation;
    private final int maxAnswersCount;
    private final int testQuestionCount;
    private final double passRate;

}
