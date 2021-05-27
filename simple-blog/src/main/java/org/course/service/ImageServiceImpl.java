package org.course.service;

import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.course.api.response.ImageUrl;
import org.course.filestore.FileStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl {

    private final FileStore fileStore;

    public ImageUrl uploadImage(MultipartFile file) {

        isFileEmpty(file);

        isImage(file);

        Map<String, String> metadata = extractMetadata(file);

        String imageId = UUID.randomUUID().toString();

        try {
            fileStore.save("belfanio-image-upload", imageId, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);//todo change to business exe
        }

        return new ImageUrl("/images/" + imageId);
    }

    public byte[] downloadImage(String id) {
        return fileStore.download("belfanio-image-upload", id);
    }

    public ImageUrl uploadTransformedImage(MultipartFile image,double width,double height) {

        isFileEmpty(image);

        isImage(image);

        String contentType = image.getContentType();

        String imageId = UUID.randomUUID().toString();

        try {

            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            BufferedImage subImage = cropToHeaderSize(bufferedImage, width, height);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            ImageIO.write(subImage, "jpg", byteArrayOutputStream);

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-type", contentType);
            metadata.put("Content-length", String.valueOf(byteArray.length));

            fileStore.save("belfanio-image-upload", imageId, Optional.of(metadata), byteArrayInputStream);

        } catch (IOException e) {
            throw new IllegalStateException(e);//todo change to business exe
        }

        return new ImageUrl("/images/" + imageId);
    }

    private BufferedImage cropToHeaderSize(BufferedImage image, double width, double height){

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        if (originalWidth > originalHeight){
            int newWidth = (int) ((width / height) * originalHeight);
            if (newWidth > originalWidth){
                int newHeight = (int) ((height / width) * originalWidth);
                return image.getSubimage(0, 0, originalWidth, newHeight);
            }
            return image.getSubimage((originalWidth - newWidth) / 2, 0, newWidth, originalHeight);
        } else {
            int newHeight = (int) ((height / width) * originalWidth);
            return image.getSubimage(0,  (originalHeight - newHeight) / 2, originalWidth, newHeight);
        }
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        System.out.println(file.getContentType());
        metadata.put("Content-type", file.getContentType());
        metadata.put("Content-length", String.valueOf(file.getSize()));
        return metadata;
    }

    //todo perform size check!!!
    private void isImage(MultipartFile file) {
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType()
                ,ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType())
                .contains(file.getContentType())){
            throw new IllegalStateException("File must be an image");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()){
            throw new IllegalStateException("Can not upload empty file " + file.getSize() + " b");
        }
    }

}
