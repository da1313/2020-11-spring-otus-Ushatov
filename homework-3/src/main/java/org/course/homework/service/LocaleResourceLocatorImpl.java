package org.course.homework.service;

import lombok.AllArgsConstructor;
import org.course.homework.config.TestProperties;
import org.course.homework.service.interfaces.LocaleResourceLocator;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Locale;


@Service
@AllArgsConstructor
public class LocaleResourceLocatorImpl implements LocaleResourceLocator {

    private final TestProperties testProperties;

    @Override
    public String getResource() {
        Locale locale = testProperties.getLocale();
        if (locale == null){
            locale = Locale.getDefault();
        }
        String csvNamePrefix = testProperties.getCsvNamePrefix();
        String csvLocation = testProperties.getCsvLocation();
        String localeNamePrefix = testProperties.getLocaleNamePrefix();
        String localeLocation = testProperties.getLocaleLocation();
        String csvPath =  getResourcePath(locale, csvNamePrefix, csvLocation, ".csv");
        URL csvResource = this.getClass().getClassLoader().getResource(csvPath);
        String bundlePath = getResourcePath(locale, localeNamePrefix, localeLocation, ".properties");
        URL bundleResource = this.getClass().getClassLoader().getResource(bundlePath);
        if (csvResource == null || bundleResource == null){
            locale = Locale.getDefault();
            String defaultCsvPath = getResourcePath(locale, csvNamePrefix, csvLocation, ".csv");
            URL defaultCsvResource = this.getClass().getClassLoader().getResource(defaultCsvPath);
            if (defaultCsvResource == null) throw new IllegalArgumentException("Can't find default question file! " + defaultCsvPath);
            String defaultBundlePath = getResourcePath(locale, localeNamePrefix, localeLocation, ".properties");
            URL defaultBundleResource = this.getClass().getClassLoader().getResource(defaultBundlePath);
            if (defaultBundleResource == null) throw  new IllegalArgumentException("Can't find default localization file! " + defaultBundlePath);
            return defaultCsvPath;
        }
        return csvPath;
    }

    private String getResourcePath(Locale locale, String fileNamePrefix, String fileNameLocation, String extension) {
        String resource;
        if (testProperties.getLocale() != null){
            resource = fileNamePrefix + "_" + locale.getLanguage() + extension;
        } else {
            resource = fileNamePrefix + "_" + Locale.getDefault().getLanguage() + extension;
        }
        if (fileNameLocation != null){
            if (fileNameLocation.equals("")){
                return resource;
            }
            return fileNameLocation.endsWith("/")? fileNameLocation : fileNameLocation + "/" + resource;
        } else {
            return resource;
        }
    }
}
