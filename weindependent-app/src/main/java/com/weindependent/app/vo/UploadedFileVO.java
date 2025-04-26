package com.weindependent.app.vo;

import lombok.Data;

@Data
public class UploadedFileVO {
    private String fileId;
    private String fileName;
    private String fileType;
    private String filePath;
}
