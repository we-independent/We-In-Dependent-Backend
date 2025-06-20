package com.weindependent.app.controller;

import com.weindependent.app.service.IBlogPdfDriveManagerService;
import com.weindependent.app.service.IBlogPdfExportService;
import com.weindependent.app.database.dataobject.UserDO;
import com.weindependent.app.exception.ResponseException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import com.weindependent.app.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpHeaders;
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
        System.out.println("💡 blogId 类型：" + (blogId != null ? blogId.getClass().getName() : "null"));
        System.out.println("✅ 正在访问 export 接口，路径 blogId = " + request.getRequestURI());
        System.out.println("🔥 请求路径：" + request.getRequestURI());

        //Step 1 Check if user log in already, if yes, next move, otherwise login first
        if (!StpUtil.isLogin()) {
            String targetUrl = request.getRequestURI();
            throw new ResponseException(401, "请先登录后再下载 PDF");
        }
        // Step 2: 获取登录的 userId（loginId 是 string 类型）
        Long userId = StpUtil.getLoginIdAsLong();

        // Step 2.1: 查询数据库中是否存在该用户
        UserDO user = userService.findUserById(userId);
        if (user == null) {
            throw new ResponseException(403, "用户未注册，请先注册");
        }

        LocalDateTime now = LocalDateTime.now(); // 当前时间
        int downloadCount = blogPdfDriveManagerService.getDownloadCount(blogId.longValue());

        // // 测试时可使用写死的userid
        // Long userId = 1L;
        // LocalDateTime now = LocalDateTime.now();


        //Step 2 After user login, now handling pdf download request 
        byte[] pdfBytes = blogPdfExportService.generatePdf(blogId);
        //Step 2.2 Check if blog already in google drive
        String existingDriveUrl = blogPdfDriveManagerService.handlePdfDownload(blogId, pdfBytes, userId.intValue(), downloadCount, now, false);
        log.info("📂 existingDriveUrl = {}", existingDriveUrl);

        if (existingDriveUrl == null || !isValidDriveUrl(existingDriveUrl)) {
            if (downloadCount >= 5) {
                byte[] regenerated = blogPdfExportService.generatePdf(blogId);
                existingDriveUrl = blogPdfDriveManagerService.handlePdfDownload(
                    blogId, regenerated, userId.intValue(), downloadCount, now, true
                );
            }
        }

        if (existingDriveUrl != null && isValidDriveUrl(existingDriveUrl)) {
            Map<String, String> result = new HashMap<>();
            result.put("downloadUrl", existingDriveUrl);
            return result;
        }

        byte[] finalPdf = (pdfBytes != null) ? pdfBytes : blogPdfExportService.generatePdf(blogId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=WeIndependent_blog_" + blogId + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(finalPdf);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("类型转换错误：访问路径 {}, 错误参数 = {}, 错误信息 = {}",
                request.getRequestURI(),
                e.getValue(),
                e.getMessage());
        return ResponseEntity.badRequest().body("❌ blogId 必须是整数");
    }

    private boolean isValidDriveUrl(String driveUrl) {
        // 非空、非非法链接的简单判断
        return driveUrl != null && driveUrl.contains("drive.usercontent.google.com") && driveUrl.contains("export=download");
    }
}