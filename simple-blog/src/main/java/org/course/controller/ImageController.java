package org.course.controller;

import lombok.RequiredArgsConstructor;
import org.course.api.response.ImageUrl;
import org.course.service.ImageServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageServiceImpl imageService;

    @PostMapping(
            path = "/images/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ImageUrl uploadImage(@RequestParam("image") MultipartFile image){
        return imageService.uploadImage(image);
    }

    @GetMapping("/images/{id}")
    public byte[] downloadImage(@PathVariable("id") String id){
        return imageService.downloadImage(id);
    }

    @PostMapping(
            path = "/images/header/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ImageUrl uploadHeaderImage(@RequestParam("image") MultipartFile image,
                                      @RequestParam("xScale") int xScale,
                                      @RequestParam("yScale") int yScale){
        return imageService.uploadTransformedImage(image, xScale, yScale);
    }

}
