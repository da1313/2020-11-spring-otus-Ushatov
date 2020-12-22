package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.config.TestProperties;
import org.course.homework.service.interfaces.LocaleResourceLocator;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@AllArgsConstructor
public class LocaleResourceLocatorImpl implements LocaleResourceLocator {

    private final TestProperties testProperties;

    @Override
    public String getResource() {
        String fileName = testProperties.getCsvFileName().split("\\.")[0];
        String localeFileName = fileName + "_" + testProperties.getLocale() + ".csv";
        URL localeResource = this.getClass().getClassLoader().getResource(localeFileName);
        return localeResource == null? testProperties.getCsvFileName() : localeFileName;
    }
}
