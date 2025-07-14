package com.weindependent.app.service;

import java.time.LocalDateTime;

public interface IBlogPdfDriveManagerService {
    String handlePdfDownload(Integer blogId, byte[] pdfBytes, Integer userId,int downloadCount, LocalDateTime now, boolean forceUpload);
    int getDownloadCount(Long blogId);
    void insertDownloadLog(Integer blogId, Integer userId, LocalDateTime now, String downloadUrl);
    String getExistingDownloadUrl(Integer blogId);
    void handlePdfRegeneration(Integer blogId);
}
