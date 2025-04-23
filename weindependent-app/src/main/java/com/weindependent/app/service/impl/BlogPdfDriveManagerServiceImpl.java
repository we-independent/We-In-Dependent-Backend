package com.weindependent.app.service.impl;

import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.database.dataobject.BlogPdfDownloadLogDO;
import com.weindependent.app.database.dataobject.BlogPdfStorageDO;
import com.weindependent.app.database.mapper.weindependent.BlogPdfExportMapper;
import com.weindependent.app.database.mapper.weindependent.BlogPdfDownloadLogMapper;
import com.weindependent.app.service.FileService;
import com.weindependent.app.service.IBlogPdfDriveManagerService;
import com.weindependent.app.vo.UploadPdfVO;
import com.weindependent.app.vo.UploadedFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

//user download PDF storage in google drive
@Slf4j
@Service("blogPdfDriverManagerService")
public class BlogPdfDriveManagerServiceImpl implements IBlogPdfDriveManagerService{
    
    @Autowired
    private FileService fileService;

    @Autowired
    private BlogPdfExportMapper blogPdfExportMapper;

    @Autowired
    private BlogPdfDownloadLogMapper blogPdfDownloadLogMapper;
    /**
     * 统一处理 PDF 下载请求：
     * - 记录下载日志到blog_pdf_download_logs表
     * - 下载次数 >=5 时，上传 PDF 到 Google Drive，并插入 blog_pdf 表
     * - 如果 dashboard blog_pdf 已有记录，则更新时间并返回链接
     *
     * @param blogId 博客文章ID
     * @param pdfBytes PDF内容
     * @param userId 当前用户ID
     * @param downloadCount 当前下载次数
     * @param now 当前时间戳
     * @return Google Drive 下载链接，如果没有则返回 null（继续返回本地 PDF）
     */
    @Override
    public String handlePdfDownload(Integer blogId, byte[] pdfBytes, Integer userId,int downloadCount, LocalDateTime now){
        //1. 日志记录
        BlogPdfDownloadLogDO downloadLog = new BlogPdfDownloadLogDO();
        downloadLog.setBlogId(blogId);
        downloadLog.setUserId(userId.longValue());
        downloadLog.setDownloadTime(now);
        downloadLog.setIsDeleted(0);
        downloadLog.setUpdateUserId(userId.longValue());
        downloadLog.setUpdateTime(now);
        blogPdfDownloadLogMapper.insertLog(downloadLog);

        //2: 是否已有 Drive 链接
        BlogPdfDO existing = blogPdfExportMapper.selectByArticleIdIgnoreDeleted(blogId);
        if (existing != null && existing.getDownloadUrl() != null) {
            String existingUrl = existing.getDownloadUrl();
            if (existingUrl.contains("/file/d/")) {
                String fileId = extractDriveFieldId(existingUrl);
                if (fileId != null) {
                    String downloadUrl = buildDownloadUrlFromDriveView(fileId);
                    existing.setDownloadUrl(downloadUrl);
                    existing.setUpdateUserId(userId);
                    existing.setUpdateTime(now);
                    blogPdfExportMapper.updateById(existing);
                    return existing.getDownloadUrl();
                }
            }
            // 如果本来就是 drive.usercontent 的下载链接，则直接返回
            if (existingUrl.contains("drive.usercontent.google.com") && existingUrl.contains("export=download")) {
                return existingUrl;
            }
        }
        log.info("是否已存在：{}", existing != null);

        //3.当达到阈值，将生成的pdf上传到Google drive中
        if (downloadCount >= 5 && pdfBytes != null && pdfBytes.length > 0) {
            try {
                // 生成临时文件
                String fileName = "WeIndependent_blog_" + blogId + ".pdf";
                Date generationTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

                // 不适用multipartfile的方法，用writePdfToTempFile的方法
                // 先写入临时文件
                File tempFile = writePdfToTempFile(pdfBytes, "WeIndependent_blog_" + blogId); // ✅

                // MultipartFile multipartFile = new MockMultipartFile(
                //         fileName, fileName, "application/pdf", pdfBytes
                // );
                // 使用 File 进行上传
                UploadPdfVO uploaded = fileService.uploadPdfFile(tempFile, fileName);
                log.info("🎯 fileId from UploadPdfVO = {}", uploaded.getFileId());
                log.info("Google Drive 上传返回链接: {}", uploaded.getFilePath());

                // 上传后删除临时文件
                tempFile.delete(); 
                String downloadUrl = uploaded.getFilePath();    
                log.info("拿到的链接是: {}", downloadUrl);

                // 插入或更新 blog_pdf 表
                if (existing != null) {
                    // 执行更新
                    BlogPdfDO newPdf = new BlogPdfDO();
                    newPdf.setId(existing.getId()); // 必须 setId，否则 updateById 会变 insert
                    newPdf.setArticleId(blogId.longValue());
                    newPdf.setFileName(fileName);
                    newPdf.setDownloadUrl(downloadUrl);
                    newPdf.setFilePath(null);
                    newPdf.setPdfFileGenerationTime(generationTime);
                    newPdf.setCreateUserId(existing.getCreateUserId());
                    newPdf.setCreateTime(existing.getCreateTime());
                    newPdf.setUpdateUserId(userId);
                    newPdf.setUpdateTime(now);
                    newPdf.setIsDeleted(0);
                    blogPdfExportMapper.updateById(newPdf); // ✅更新而不是插入
                } else {
                    // 执行插入
                    BlogPdfDO newPdf = new BlogPdfDO();
                    newPdf.setArticleId(blogId.longValue());
                    newPdf.setFileName(fileName);
                    newPdf.setDownloadUrl(downloadUrl);
                    newPdf.setFilePath(null);
                    newPdf.setPdfFileGenerationTime(generationTime);
                    newPdf.setCreateUserId(userId);
                    newPdf.setCreateTime(now);
                    newPdf.setUpdateUserId(userId);
                    newPdf.setUpdateTime(now);
                    newPdf.setIsDeleted(0);
                    blogPdfExportMapper.insertPdf(newPdf); // ✅插入
                }
                
                // 插入或更新 blog_pdf_storage 表
                BlogPdfStorageDO storage = blogPdfExportMapper.selectStorageByBlogId(blogId);
                if (storage != null) {
                    storage.setPdfContent(pdfBytes);
                    storage.setFileName(fileName);
                    storage.setUpdateUserId(userId);
                    storage.setUpdateTime(now);
                    blogPdfExportMapper.updateStorageByBlogId(storage);
                } else {
                    BlogPdfStorageDO newStorage = new BlogPdfStorageDO();
                    newStorage.setBlogId(blogId);
                    newStorage.setPdfContent(pdfBytes);
                    newStorage.setFileName(fileName);
                    newStorage.setPdfGenerationTime(generationTime);
                    newStorage.setIsDeleted(0);
                    newStorage.setCreateUserId(userId);
                    newStorage.setCreateTime(now);
                    newStorage.setUpdateUserId(userId);
                    newStorage.setUpdateTime(now);
                    blogPdfExportMapper.insertStorage(newStorage);
                }
                log.info("最终 downloadUrl：{}", downloadUrl);
                return downloadUrl;
            }catch (Exception e) {
                throw new RuntimeException("Google Drive 上传失败", e);
            }
        }else if (downloadCount >= 5 && (pdfBytes == null || pdfBytes.length == 0)) {
            log.warn("⚠️ PDF 内容为空，跳过上传 Google Drive");
        }

        //4: 返回 null，控制层将继续返回本地字节流
        return null;

    }

    private String buildDownloadUrlFromDriveView(String viewUrl) {
        String fileId = extractDriveFieldId(viewUrl);
        return fileId == null ? null : "https://drive.usercontent.google.com/uc?id=" + fileId + "&export=download";
    }

    private String extractDriveFieldId(String url){
        // 提取 https://drive.google.com/file/d/FILE_ID/view 的 FILE_ID
        Pattern pattern = Pattern.compile("file/d/(.*?)/view");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1); // group(1) 就是 fileId
        }
        return null;
    }

    // 新增方法：将 PDF 字节写入临时文件
    private File writePdfToTempFile(byte[] pdfBytes, String fileName) throws IOException {
        File tempFile = File.createTempFile(fileName, ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(pdfBytes);
        }
        return tempFile;
    }
}
