package com.weindependent.app.service.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.database.dataobject.BlogPdfDownloadLogDO;
import com.weindependent.app.database.mapper.weindependent.BlogPdfDownloadLogMapper;
import com.weindependent.app.database.mapper.weindependent.BlogPdfExportMapper;
import com.weindependent.app.service.FileService;
import com.weindependent.app.service.IBlogPdfDriveManagerService;
import com.weindependent.app.vo.UploadedFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service("blogPdfDriverManagerService")
public class BlogPdfDriveManagerServiceImpl implements IBlogPdfDriveManagerService {

    @Value("${google.drive.folder.blog-pdf}")
    private String blogPdfFolderId;

    @Autowired
    private FileService fileService;

    @Autowired
    private BlogPdfExportMapper blogPdfExportMapper;

    @Autowired
    private BlogPdfDownloadLogMapper blogPdfDownloadLogMapper;

    @Override
    public String handlePdfDownload(Integer blogId, byte[] pdfBytes, Integer userId, int downloadCount, LocalDateTime now, boolean forceUpload) {
        BlogPdfDO existing = blogPdfExportMapper.selectByArticleIdIgnoreDeleted(blogId);

        // 优先返回已有有效链接
        if (!forceUpload && existing != null && existing.getDownloadUrl() != null) {
            String url = existing.getDownloadUrl();
            if (isValidDriveDownloadLink(url)) {
                if (downloadCount >= 5) {
                    insertDownloadLog(blogId, userId, now, url);
                    log.info("✅ 已存在有效 Drive 下载链接，跳过上传: {}", url);
                    return url;
                } else {
                    log.info("📌 已存在 Drive 下载链接，但未达下载门槛 ({} 次)，不返回链接", downloadCount);
                }
            }
            // view 链接，转换并更新
            String fileId = extractDriveFieldId(url);
            if (fileId != null) {
                String newUrl = buildDownloadUrlFromDriveView(fileId);
                existing.setDownloadUrl(newUrl);
                existing.setUpdateUserId(userId);
                existing.setFileKey(fileId);
                existing.setUpdateTime(now);
                blogPdfExportMapper.updateById(existing);
                insertDownloadLog(blogId, userId, now, newUrl);
                return newUrl;
            }
        }

        Drive drive = ((FileServiceImpl) fileService).getDrive();

        // 检查 Drive 中是否已存在同名文件（仅当数据库中无记录，且下载次数达标）
        if (!forceUpload && existing == null && downloadCount >= 5) {
            try {
                String fileName = "WeIndependent_blog_" + blogId + ".pdf";
                String query = String.format("name = '%s' and trashed = false and '%s' in parents", fileName, blogPdfFolderId);
                FileList result = drive.files().list()
                        .setQ(query)
                        .setSpaces("drive")
                        .setFields("files(id, name)")
                        .execute();
                List<com.google.api.services.drive.model.File> files = result.getFiles();

                if (files != null && !files.isEmpty()) {
                    String downloadUrl = buildDownloadUrlFromDriveView(files.get(0).getId());
                    log.info("Drive 中已存在同名文件，跳过上传: {}", downloadUrl);
                    insertDownloadLog(blogId, userId, now, downloadUrl);
                    return downloadUrl;
                }
            } catch (Exception e) {
                log.warn("Drive 查询失败，准备上传: {}", e.getMessage());
            }
        }

        // 满足上传条件或强制上传
        if ((downloadCount >= 5 || forceUpload) && pdfBytes != null && pdfBytes.length > 0) {
            try {
                String fileName = "WeIndependent_blog_" + blogId + ".pdf";
                MultipartFile file = new MockMultipartFile(fileName, fileName, "application/pdf", pdfBytes);
                UploadedFileVO uploadedFileVo = fileService.uploadFile(file, fileName, "blog-pdf");
                String viewUrl = uploadedFileVo.getFilePath();
                if (viewUrl == null || viewUrl.isEmpty()) throw new RuntimeException("上传成功但返回空链接");
                // String fileId = extractDriveFieldId(viewUrl);
                String fileId = uploadedFileVo.getFileKey();
                String downloadUrl = fileId != null ? buildDownloadUrlFromDriveView(fileId) : viewUrl;
                log.info("📦 上传成功, downloadUrl: {}", downloadUrl);

                Date generationTime = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

                BlogPdfDO newPdf = new BlogPdfDO();
                newPdf.setArticleId(blogId.longValue());
                newPdf.setFileName(fileName);
                newPdf.setDownloadUrl(downloadUrl);
                newPdf.setFilePath(null);
                newPdf.setFileKey(fileId);
                newPdf.setPdfFileGenerationTime(generationTime);
                newPdf.setIsDeleted(0);
                newPdf.setUpdateTime(now);
                newPdf.setUpdateUserId(userId);

                if (existing != null) {
                    newPdf.setId(existing.getId());
                    newPdf.setCreateUserId(existing.getCreateUserId());
                    newPdf.setCreateTime(existing.getCreateTime());
                    blogPdfExportMapper.updateById(newPdf);
                } else {
                    newPdf.setCreateUserId(userId);
                    newPdf.setCreateTime(now);
                    blogPdfExportMapper.insertPdf(newPdf);
                }

                insertDownloadLog(blogId, userId, now, downloadUrl);
                return downloadUrl;

            } catch (Exception e) {
                log.error("❌ PDF 上传失败", e);
                throw new RuntimeException("上传失败", e);
            }
        } else {
            log.warn("⚠️ 条件不满足，未上传 PDF");
        }

        // 未满足上传，返回 null
        insertDownloadLog(blogId, userId, now, null);
        return null;
    }
    @Override
    public int getDownloadCount(Long blogId) {
        return blogPdfDownloadLogMapper.getDownloadCount(blogId);
    }
    private void insertDownloadLog(Integer blogId, Integer userId, LocalDateTime now, String downloadUrl) {
        BlogPdfDownloadLogDO logDO = new BlogPdfDownloadLogDO();
        logDO.setBlogId(blogId);
        logDO.setUserId(userId.longValue());
        logDO.setDownloadTime(now);
        logDO.setDownloadUrl(downloadUrl);
        logDO.setIsDeleted(0);
        logDO.setUpdateUserId(userId.longValue());
        logDO.setUpdateTime(now);
        blogPdfDownloadLogMapper.insertLog(logDO);
    }

    private String extractDriveFieldId(String url) {
        Matcher matcher = Pattern.compile("file/d/([^/?]+)").matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    private boolean isValidDriveDownloadLink(String url) {
        return url != null && url.contains("drive.usercontent.google.com") && url.contains("export=download");
    }

    private String buildDownloadUrlFromDriveView(String fileId) {
        return "https://drive.usercontent.google.com/uc?id=" + fileId + "&export=download";
    }
}
