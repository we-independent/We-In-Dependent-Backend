package com.weindependent.app.controller;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.IBlogArticleService;
import com.weindependent.app.service.IBlogPdfExportService;
import com.weindependent.app.service.IBlogPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;
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
    private IBlogArticleListService blogArticleListService;
    /**
     * 查询博客文章列表 Hurely
     */
    // @SignatureAuth
    @Operation(summary = "查询博客文章列表")
    @PostMapping("/articlelist")
    public PageInfo<BlogArticleListDO> listPdf(
        @RequestHeader(name = "version", required = true) String version,
        @RequestBody BlogArticleListQry query
    ) {
        return blogArticleListService.selectBlogArticleList(query);
    }

}