package com.weindependent.app.controller;

import com.weindependent.app.service.IBlogPdfDriveManagerService;
import com.weindependent.app.service.IBlogPdfExportService;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.weindependent.app.service.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import cn.dev33.satoken.stp.StpUtil;

@Slf4j
@Tag(name = "博客文章PDF下载")
@RestController
@RequestMapping("/api")
public class ExportPdfController {

    //生成PDF（动态生成)    
    @Autowired
    private IBlogPdfExportService blogPdfExportService;

    @Autowired
    private UserService userService;    

    @Autowired
    private IBlogPdfDriveManagerService blogPdfDriveManagerService;

    /**
     * 导出博客文章 PDF
     *
     * @param blogId 博客文章ID
     * @return PDF 文件流
     * 
     * @author Hurely
     * 
     */

    @Operation(summary = "Download_Blog_Pdf")
    @GetMapping("/export/{blogId}")
    public Object exportPdf(@PathVariable Integer blogId, HttpServletRequest request) {
        log.info("✅ 正在访问 export 接口，blogId = {}", blogId);

        // Step 1: 校验登录
        if (!StpUtil.isLogin()) {
            throw new ResponseException(401, "请先登录后再下载 PDF");
        }

        Long userId = StpUtil.getLoginIdAsLong();
        UserDO user = userService.findUserById(userId);
        if (user == null) {
            throw new ResponseException(403, "用户未注册，请先注册");
        }

        LocalDateTime now = LocalDateTime.now();
        int downloadCount = blogPdfDriveManagerService.getDownloadCount(blogId.longValue());

        // Step 2: 检查是否已有有效的下载链接
        String existingDriveUrl = blogPdfDriveManagerService.getExistingDownloadUrl(blogId);
        if (existingDriveUrl != null && isValidDriveUrl(existingDriveUrl)) {
            log.info("✅ 发现有效 downloadUrl，直接返回 JSON: {}", existingDriveUrl);
            Map<String, Object> result = new HashMap<>();
            result.put("downloadUrl", existingDriveUrl);
            return result;
        }

        // Step 3: 没有链接 → 生成 PDF → 上传 → 返回新链接
        byte[] pdfBytes = blogPdfExportService.generatePdf(blogId);
        String newUrl = blogPdfDriveManagerService.handlePdfDownload(blogId, pdfBytes, userId.intValue(), downloadCount, now, true);

        if (newUrl == null) {
            throw new ResponseException(500, "PDF 生成或上传失败");
        }

        log.info("📦 PDF 上传完成，返回新链接: {}", newUrl);
        Map<String, String> result = new HashMap<>();
        result.put("downloadUrl", newUrl);
        return result;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("类型转换错误：访问路径 {}, 错误参数 = {}, 错误信息 = {}",
                request.getRequestURI(),
                e.getValue(),
                e.getMessage());
        return ResponseEntity.badRequest().body("❌ blogId 必须是整数");
    }

    @Operation(summary = "用于统计下载次数")
    @GetMapping("/export/download-count/{id}")
    public Integer getDownloadCount(@PathVariable Long id) {
        return blogPdfDriveManagerService.getDownloadCount(id);
    }

    @Operation(summary = "下载次数不达标，走数据流生成pdf")
    @GetMapping("/export/raw/{id}")
    public void exportRawPdf(@PathVariable Integer id, HttpServletResponse response) {
        if (!StpUtil.isLogin()) {
            throw new ResponseException(401, "请先登录后再下载 PDF");
        }

        Long userId = StpUtil.getLoginIdAsLong();
        
        byte[] pdfBytes = blogPdfExportService.generatePdf(id);
        String fileName = "WeIndependent_blog_" + id + ".pdf";

        // log.info("📦 PDF 字节生成结果: {}", (pdfBytes == null) ? "null" : ("长度 = " + pdfBytes.length));

        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new ResponseException(ErrorCode.BAD_REQUEST.getCode(), "生成 PDF 失败");
        }

        blogPdfDriveManagerService.insertDownloadLog(id, userId.intValue(), LocalDateTime.now(), null);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        try (OutputStream out = response.getOutputStream()) {
            out.write(pdfBytes);
            out.flush();
        } catch (IOException e) {
            log.error("❌ PDF 输出失败", e);
            throw new ResponseException(ErrorCode.BAD_REQUEST.getCode(), "PDF 下载失败");
        }
    }

    
    private boolean isValidDriveUrl(String driveUrl) {
        // 非空、非非法链接的简单判断
        return driveUrl != null && driveUrl.contains("drive.usercontent.google.com") && driveUrl.contains("export=download");
    }
}