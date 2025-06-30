package com.weindependent.app.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageResizeUtil {
    public static MultipartFile resizeImage(MultipartFile file, Integer width, Integer height) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .forceSize(width, height)
                .asBufferedImage();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String formatName = getFormatName(file.getOriginalFilename());
        ImageIO.write(resizedImage, formatName, os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

        return new MockMultipartFile(file.getName(), file.getOriginalFilename(), file.getContentType(), is);
    }

    private static String getFormatName(String filename) {
        if (filename == null) {
            return "jpg";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "jpg";
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }
}
