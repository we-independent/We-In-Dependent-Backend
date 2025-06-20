package com.weindependent.app.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * 解决request流只读取一次的问题
 * @desc 此类覆写影响multipart/form-data的请求
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    /**
     * 存储body数据的容器
     */
    private final byte[] body;
    private final boolean isMultipart;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        // 将body数据存储起来
        String contentType = request.getContentType();
        isMultipart = contentType != null && contentType.startsWith("multipart/form-data");

        if (isMultipart) {
            // 对于multipart请求，不读取body
            body = new byte[0];
        } else {
            // 非multipart请求，读取body内容
            body = getBodyString(request).getBytes(Charset.defaultCharset());
        }
    }

    /**
     * 获取请求Body
     *
     * @param request request
     * @return String
     */
    public String getBodyString(final ServletRequest request) {
        try {
            return inputStream2String(request.getInputStream());
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求Body
     *
     * @return String
     */
    public String getBodyString() {
        final InputStream inputStream = new ByteArrayInputStream(body);

        return inputStream2String(inputStream);
    }

    /**
     * 将inputStream里的数据读取出来并转换成字符串
     *
     * @param inputStream inputStream
     * @return String
     */
    private String inputStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }

        return sb.toString();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (isMultipart) {
            return super.getReader(); // 返回原始Reader
        } else {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (isMultipart) {
            // 返回原始InputStream
            return super.getInputStream();
        } else {
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return inputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }
            };
        }
    }
}
