package com.weindependent.app.vo;

import lombok.Data;

@Data
public class UploadedFileVO {
    private Integer fileId;
    private String fileKey;
    private String fileName;
    private String fileType;
    private String filePath;
}
