package com.weindependent.app.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CommonUtil {

    public static String getRequestBody(HttpServletRequest request) {
        try (InputStream is = request.getInputStream()) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("read http request failed.", e);
        }
        return null;
    }

    public static String convertToImgSrc(String driveUrl, int resolution) {

        if(driveUrl == null || driveUrl.isEmpty())
            return driveUrl;

        if(driveUrl.startsWith("http://") || driveUrl.startsWith("https://")) {
            return driveUrl; // Already a full URL
        }
        
        String regex = "https://drive.google.com/file/d/([a-zA-Z0-9_-]+)(?:/view.*)?";
        String fileId;

        if (driveUrl.matches(regex)) {
            fileId = driveUrl.replaceAll(regex, "$1");
        } else {
            throw new IllegalArgumentException("Invalid Google Drive URL:" + driveUrl);
        }

        return "https://lh3.googleusercontent.com/d/" + fileId + "=s" + resolution;
    }


}
