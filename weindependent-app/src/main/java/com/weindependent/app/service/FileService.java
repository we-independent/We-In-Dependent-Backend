package com.weindependent.app.service;
import com.weindependent.app.vo.UploadPdfVO;
import com.weindependent.app.vo.UploadedFileVO;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    UploadedFileVO uploadFile(MultipartFile file, String category);
    boolean deleteFile(String filePath);

    //Hurely add for pdf upload to google drive
    UploadPdfVO uploadPdfFile(File tempFile, String filenameString);
}
