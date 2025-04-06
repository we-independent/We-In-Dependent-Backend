package com.weindependent.app.controller;
import com.weindependent.app.service.IBlogArticleService;
import com.weindependent.app.service.IBlogPdfExportService;
import com.weindependent.app.service.IBlogPdfListService;
import com.weindependent.app.service.IBlogPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogArticlePdfListDO;
import com.weindependent.app.dto.BlogArticlePdfListQry;
import com.weindependent.app.dto.BlogArticleQry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "博客文章PDF List获取")
@RestController
@RequestMapping("/")
public class GetBlogList {

    @Autowired  // ✅ 确保加上这个注解
    private IBlogPdfListService blogPdfListService;
    /**
     * 查询博客文章列表 Hurely
     */
    // @SignatureAuth
    @Operation(summary = "查询博客文章列表")
    @PostMapping("/pdflist")
    public PageInfo<BlogArticlePdfListDO> listPdf(
        @RequestHeader(name = "version", required = true) String version,
        @RequestBody BlogArticlePdfListQry query
    ) {
        return blogPdfListService.selectBlogPdfList(query);
    }
}