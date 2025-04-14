
package com.weindependent.app.controller;

import com.weindependent.app.service.IBlogArticleListService;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "博客文章Article List获取")
@RestController
@RequestMapping("/")
public class GetBlogListController {

    @Autowired
    private IBlogArticleListService blogArticleListService;

    @Operation(summary = "通过Category_id获得所有相关Article并默认按update_time desc排序")
    @PostMapping("/articles/by-category")
    public PageInfo<BlogArticleListDO> listPdf(
            @RequestHeader(name = "version", required = true) String version,
            @RequestBody BlogArticleListQry query
    ) {
        return blogArticleListService.selectBlogArticleList(query);
    }

    @Operation(summary = "冷启动推荐博客文章")
    @GetMapping("/articles/coldstart")
    public ResponseEntity<List<BlogArticleListDO>> getColdStartArticles() {
        List<BlogArticleListDO> coldStartArticles = blogArticleListService.selectColdStartArticles();
        return ResponseEntity.ok(coldStartArticles);
    }
}
