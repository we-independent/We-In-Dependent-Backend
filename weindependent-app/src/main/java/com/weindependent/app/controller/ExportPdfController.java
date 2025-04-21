package com.weindependent.app.controller;
import com.weindependent.app.service.IBlogPdfDownloadService;
import com.weindependent.app.service.IBlogPdfExportService;
import com.weindependent.app.database.dataobject.BlogPdfStorageDO;
import com.weindependent.app.database.dataobject.UserDO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.weindependent.app.service.IBlogPdfStorageService;
import com.weindependent.app.service.UserService;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import cn.dev33.satoken.stp.StpUtil;


@Tag(name = "博客文章PDF下载")
@RestController
@RequestMapping("/api")
public class ExportPdfController {

    //生成PDF（动态生成)
    @Autowired
    private IBlogPdfExportService blogPdfExportService;

    // 用于操作数据库中PDF存储记录，存入BLOB字段
    @Autowired
    private IBlogPdfStorageService blogPdfStorageService;

    // 用于记录每次下载操作的日志（保存下载用户、下载时间等）
    @Autowired
    private IBlogPdfDownloadService blogPdfDownloadService;

    @Autowired
    private UserService userService;    

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
    public ResponseEntity<byte[]> exportPdf(@PathVariable Integer blogId, HttpServletRequest request) {

        //Step 1 Check if user log in already, if yes, next move, otherwise login first
        if (!StpUtil.isLogin()) {
            String targetUrl = request.getRequestURI();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/user/login?targetUrl=" + targetUrl);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        // Step 2: 获取登录的 userId（loginId 是 string 类型）
        Long userId = StpUtil.getLoginIdAsLong();

        // Step 2.1: 查询数据库中是否存在该用户
        UserDO user = userService.findUserById(userId);
        if (user == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/user/register?account=" + userId);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        LocalDateTime now = LocalDateTime.now(); // 当前时间

        // //测试时可使用写死的userid
        // Long userId = 1;
        // LocalDateTime now = LocalDateTime.now();


        //Step 2 After user login, now handling pdf download request 
        byte[] pdfBytes = null;

        //Step 2.1 Record download activity：update time，userid and blog id. id = blogid
        blogPdfDownloadService.updateStatistics(blogId, userId, now);

        //Step 2.2 Check if blog already in DB
        BlogPdfStorageDO pdfRecord = blogPdfStorageService.getPdfByBlogId(blogId);
        if (pdfRecord != null){
            // System.out.println("✅ 已命中缓存（数据库PDF存在），blogId = " + blogId);
            pdfBytes = pdfRecord.getPdfContent();
        }else{
            pdfBytes = blogPdfExportService.generatePdf(blogId);
            // System.out.println("📄 正在动态生成 PDF, blogId = " + blogId);
            int downloadCount = blogPdfDownloadService.getDownloadCount(blogId);
            if (downloadCount >= 5) {
                blogPdfStorageService.savePdf(blogId, pdfBytes);
            }
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=WeIndependent_blog_" + blogId + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}