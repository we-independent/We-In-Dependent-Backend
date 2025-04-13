package com.weindependent.app.service;

import com.weindependent.app.vo.UploadedFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UploadedFileVO uploadFile(MultipartFile file, String category);
    boolean deleteFile(String filePath);
}
