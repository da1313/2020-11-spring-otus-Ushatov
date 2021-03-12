package org.course.service;

import lombok.AllArgsConstructor;
import org.course.service.interfaces.ImageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@AllArgsConstructor
public class ImageServiceFake implements ImageService {

    public static final String FILE = "image-storage/Манипуляторы для работы с химикатами.jpg";

    @Override
    public byte[] getImage() {
        try {
            URL resource = ClassLoader.getSystemClassLoader().getResource(FILE);
            Path path = Path.of(resource.toURI());
            return Files.readAllBytes(path);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException();
        }
    }
}
