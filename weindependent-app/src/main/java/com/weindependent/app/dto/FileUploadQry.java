package com.weindependent.app.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadQry {
    private MultipartFile file;
    private String category;
}
