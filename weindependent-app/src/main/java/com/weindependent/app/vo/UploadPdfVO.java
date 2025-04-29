package com.weindependent.app.vo;

import lombok.Data;

@Data
public class UploadPdfVO {
    private String fileId;
    private String fileName;
    private String fileType;
    private String filePath;
}
