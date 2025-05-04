package com.weindependent.app.service;

import java.time.LocalDateTime;

public interface IBlogPdfDriveManagerService {
    String handlePdfDownload(Integer blogId, byte[] pdfBytes, Integer userId,int downloadCount, LocalDateTime now, boolean forceUpload);
    int getDownloadCount(Long blogId);
}
