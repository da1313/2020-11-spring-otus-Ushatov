package org.course.service;

import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.course.api.response.ImageUrl;
import org.course.filestore.FileStore;
import org.course.filestore.ImageMetadataItem;
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

        List<ImageMetadataItem> metadataItems = getImageMetadataItems(file.getContentType(), file.getSize());

        String imageId = UUID.randomUUID().toString();

        try {
            fileStore.save("belfanio-image-upload", imageId, file.getInputStream(), file.getSize(), metadataItems);
        } catch (IOException e) {
            throw new IllegalStateException(e);//todo change to business exe
        }

        return new ImageUrl("/images/" + imageId);
    }

    public byte[] downloadImage(String id) {
        return fileStore.download("belfanio-image-upload", id);
    }

    public ImageUrl uploadTransformedImage(MultipartFile image, double width, double height) {

        isFileEmpty(image);

        isImage(image);

        String contentType = image.getContentType();

        String imageId = UUID.randomUUID().toString();

        try {

            byte[] byteArray = getResizedImageByteArray(image, width, height);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

            List<ImageMetadataItem> metadataItems = getImageMetadataItems(contentType, byteArray.length);

            fileStore.save("belfanio-image-upload", imageId, byteArrayInputStream, byteArray.length, metadataItems);

        } catch (IOException e) {
            throw new IllegalStateException(e);//todo change to business exe
        }

        return new ImageUrl("/images/" + imageId);
    }

    private byte[] getResizedImageByteArray (MultipartFile image, double width, double height) throws IOException {

        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

        BufferedImage subImage = cropToSize(bufferedImage, width, height);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(subImage, "jpg", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private List<ImageMetadataItem> getImageMetadataItems(String contentType, long size) {
        List<ImageMetadataItem> metadataItems = new ArrayList<>();
        metadataItems.add(new ImageMetadataItem("Content-type", contentType));
        metadataItems.add(new ImageMetadataItem("Content-length", String.valueOf(size)));
        return metadataItems;
    }

    private BufferedImage cropToSize(BufferedImage image, double width, double height){

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
