package com.weindependent.app.controller;
import com.weindependent.app.service.IBlogPdfExportService;
import com.weindependent.app.service.IBlogPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.weindependent.app.annotation.SignatureAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "博客文章PDF下载")
@RestController
@RequestMapping("/")
public class ExportPdfController {

    @Autowired
    private IBlogPdfExportService blogPdfService;

    /**
     * 导出博客文章 PDF
     *
     * @param id 博客文章ID
     * @return PDF 文件流
     */
    // @SignatureAuth // 测试时可以注释掉
    @Operation(summary = "Download_Blog_Pdf")
    @GetMapping("/export/{id}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Integer id) {
        byte[] pdfBytes = blogPdfService.generatePdf(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=WeIndependent_blog_" + id + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}