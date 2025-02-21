package com.weindependent.app.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CommonUtils {

    public static String getRequestBody(HttpServletRequest request) {
        try (InputStream is = request.getInputStream()) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("read http request failed.", e);
        }
        return null;
    }

}
