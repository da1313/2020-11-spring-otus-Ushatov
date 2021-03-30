package org.course.controllers;

import lombok.AllArgsConstructor;
import org.course.service.interfaces.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/image/{id}")
    public byte[] getImage(@PathVariable(value = "id", required = false) String bookId){//fake id
        return imageService.getImage();

    }

}
