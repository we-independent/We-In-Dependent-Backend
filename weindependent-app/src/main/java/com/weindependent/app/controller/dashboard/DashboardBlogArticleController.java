package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.dto.BlogArticleEditQry;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.vo.BlogArticleEditVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import com.weindependent.app.service.IBlogArticleService;
import com.weindependent.app.service.IBlogPdfDriveManagerService;
import com.weindependent.app.service.IBlogPdfExportService;

import org.springframework.web.multipart.MultipartFile;


/**
 * 博客文章Controller
 * 
 * @author christina
 *    2025-03-23
 */
@Tag(name = "博客文章管理")
@RestController
@RequestMapping("api/dashboard")
@Slf4j
public class DashboardBlogArticleController {
    private final IBlogArticleService blogArticleService;

    public DashboardBlogArticleController(IBlogArticleService blogArticleService) {
        this.blogArticleService = blogArticleService;
    }
    @Autowired
    private IBlogPdfExportService blogPdfExportService;
    @Autowired
    private IBlogPdfDriveManagerService blogPdfDriveManagerService;

    /**
     * 查询博客文章列表
     */
    @SaCheckRole("admin")
    @Operation(summary = "查询博客文章列表")
    @PostMapping("/blog/list")
    public PageInfo<BlogArticleDO> list(@RequestBody BlogArticleQry blogArticleQry) {
        return blogArticleService.selectBlogArticleList(blogArticleQry);
    }

    /**
     * 查询博客文章详细信息
     */
    @SaCheckRole("admin")
    @Operation(summary = "查询博客文章详细信息")
    @GetMapping(value = "/blog/{id}")
    public BlogArticleEditVO getInfo(@PathVariable("id") Integer id) {
        return blogArticleService.selectBlogArticleByIdForEdit(id);
    }

    /**
     * 新增博客文章
     */
    @SaCheckRole("admin")
    @Operation(summary = "新增博客文章")
    @PostMapping("/blog")
    public boolean add(@RequestBody BlogArticleEditQry blogArticle) {

        return blogArticleService.insertBlogArticle(blogArticle) > 0;
    }

    /**
     * 新增博客图片
     */
    @SaCheckRole("admin")
    @Operation(summary = "新增博客banner图片")
    @PostMapping(value = "/blog/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageDO addBlogBanner(@RequestParam("file") MultipartFile file) {
        return blogArticleService.insertBlogBanner(file);
    }

    /**
     * 修改博客文章
     */
    @SaCheckRole("admin")
    @Operation(summary = "修改博客文章")
    @PutMapping("/blog/{id}")
    public boolean edit(@PathVariable("id") Integer id, @RequestBody @Valid BlogArticleEditQry blogArticle) {

        blogArticle.setId(id);
        boolean success = blogArticleService.updateBlogArticle(blogArticle) > 0;

    if (success) {
        // ✨ 强制重新生成 PDF 并上传
        try {
            byte[] pdfBytes = blogPdfExportService.generatePdf(id);  // ⬅️ 生成 PDF 内容
            blogPdfDriveManagerService.handlePdfDownload(
                id,
                pdfBytes,
                StpUtil.getLoginIdAsInt(),
                0,  // 下载次数为 0 表示不依赖已有数据
                LocalDateTime.now(),
                true // 强制上传
            );
        } catch (Exception e) {
            log.error("❌ 更新后 PDF 上传失败", e);
            // 可以不抛出异常，只记录日志，也可以作为整体返回失败
        }
    }

        return blogArticleService.updateBlogArticle(blogArticle) > 0;
    }

    /**
     * 删除博客文章
     */
    @SaCheckRole("admin")
    @Operation(summary = "删除博客文章")
@DeleteMapping("/blog/{ids}")
public boolean remove(@PathVariable @Valid List<Integer> ids) {
    // List<Integer> idList = Arrays.stream(ids.split(","))
    //                              .map(Integer::parseInt)
    //                              .collect(Collectors.toList());
        return blogArticleService.deleteBlogArticleByIds(ids) > 0;
    }

    @SaCheckRole("admin")
    @Operation(summary = "查询博客文章列表")
    @GetMapping("/search-article-id-and-title/{keyword}")
    public List<BlogArticleDO>  searchByIdAndTitle(@PathVariable String  keyword) {
        return blogArticleService.searchByIdAndTitle(keyword);
    }
}
