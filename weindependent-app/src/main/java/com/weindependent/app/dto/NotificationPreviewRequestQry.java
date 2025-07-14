package com.weindependent.app.dto;

import lombok.Data;
import java.util.List;

@Data
public class NotificationPreviewRequestQry {
    private String type;
    private String title;
    private String content;
    private String buttonText;
    private String buttonUrl;
    private List<ImageDTO> imageList;

    @Data
    public static class ImageDTO {
        private String imageType;
        private Integer sortOrder;
        private String imageUrl;
    }
}

