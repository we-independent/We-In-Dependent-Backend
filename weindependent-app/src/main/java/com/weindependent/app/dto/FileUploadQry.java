package com.weindependent.app.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class FileUploadQry {
    private MultipartFile file;
    private String category;
    public boolean isValid(){
        return file != null && category != null && !category.isEmpty();
    }
}
