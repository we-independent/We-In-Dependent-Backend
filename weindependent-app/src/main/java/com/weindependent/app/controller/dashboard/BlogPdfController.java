package com.weindependent.app.controller.dashboard;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.net.MediaType;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.dto.BlogPdfQry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.service.IBlogPdfService;
import org.springframework.web.bind.annotation.RequestParam;




/**
 * 博客文章pdfController
 * 
 * @author christina
 *    2025-03-23
 */
@Tag(name = "博客文章pdf管理")
@RestController
@RequestMapping("/dashboard/pdf")
public class BlogPdfController
{
    private final IBlogPdfService blogPdfService;

    public BlogPdfController(IBlogPdfService blogPdfService) {
        this.blogPdfService = blogPdfService;
    }

    /**
     * 查询博客文章pdf列表
     */
    @SignatureAuth
    @Operation(summary = "查询博客文章pdf列表")
    @GetMapping("/list")
    public PageInfo<BlogPdfDO> list(@RequestBody BlogPdfQry blogPdfQry)
    {
        return blogPdfService.selectBlogPdfList(blogPdfQry);
    }

    /**
     * 查询博客文章pdf详细信息
     */
    @SignatureAuth
    @Operation(summary = "查询博客文章pdf详细信息")
    @GetMapping(value = "/{id}")
    public BlogPdfDO getInfo(@PathVariable("id") Integer id)
    {
        return blogPdfService.selectBlogPdfById(id);
    }

    /**
     * 新增博客文章pdf
     */
    @SignatureAuth
    @Operation(summary = "新增博客文章pdf")
    @PostMapping
    public boolean add(@RequestBody BlogPdfDO blogPdf)
    {
        return blogPdfService.insertBlogPdf(blogPdf) > 0;
    }

    /**
     * 修改博客文章pdf
     */
    @SignatureAuth
    @Operation(summary = "修改博客文章pdf")
    @PutMapping
    public boolean edit(@RequestBody BlogPdfDO blogPdf)
    {
        return blogPdfService.updateBlogPdf(blogPdf) > 0;
    }

    /**
     * 删除博客文章pdf
     */
    @SignatureAuth
    @Operation(summary = "删除博客文章pdf")
	@DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids)
    {
        return blogPdfService.deleteBlogPdfByIds(ids) > 0;
    }

    /**
     * 导出博客文章pdf
     * 
     * @author Hurely
     * @since 2025-04-2
     */
    @SignatureAuth // 测试时暂时注释掉
    @Operation(summary = "Download_Blog_Pdf")
    @GetMapping("/export/{id}")
    // Long blogID 兼容 database里的 int id
    public ResponseEntity<byte[]> exportPdf(@PathVariable Integer id) {
        byte[] pdfBytes = blogPdfService.generatePdf(id);
        // System.out.println("📥 正在导出博客 ID = " + id); // ✅ 测试时打印日志
        //返回 PDF 文件流
        return ResponseEntity.ok()
                             .header("Content-Disposition", "attachment; filename = WeIndependent_blog_" + id + ".pdf")
                             .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                             .body(pdfBytes);

    }
    
}
