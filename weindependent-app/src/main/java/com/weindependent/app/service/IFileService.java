package com.weindependent.app.service;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.vo.UploadedFileVO;

import org.springframework.web.multipart.MultipartFile;
import com.google.api.services.drive.Drive;

public interface IFileService {
    UploadedFileVO uploadFile(MultipartFile file, String filename , GoogleDriveFileCategoryEnum category);
    void deleteFile(String fileId);

    Drive getDrive();
}
