package com.weindependent.app.controller;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.IBlogArticleCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.database.dataobject.BlogCategoryDO;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.database.dataobject.CategoryInfoDO;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 获取文章List
 * 
 * @author Hurely
 *    2025-04-5
 */

@Tag(name = "博客文章Article List获取")
@RestController
@RequestMapping("/")
public class GetBlogListController {

    @Autowired  // ✅ 确保加上这个注解
    private IBlogArticleListService blogArticleListService;
    @Autowired
    private IBlogArticleCategoryService blogCategoryService;
    /**
     * 查询博客文章列表 Hurely
     */
    // @SignatureAuth
    @Operation(summary = "通过Category_id获得所有相关Article并默认按update_time desc排序")
    @PostMapping("/articles/by-category")
    public PageInfo<BlogArticleListDO> listPdf(
        @RequestHeader(name = "version", required = true) String version,
        @RequestBody BlogArticleListQry query
    ) {
        return blogArticleListService.selectBlogArticleList(query);
    }

    @Operation(summary = "获取所有Blog Category列表")
    @GetMapping("/category-names")
    public ResponseEntity<List<CategoryInfoDO>> getCategoryNames() {
        List<BlogCategoryDO> allCategories = blogCategoryService.selectAllCategories();
        List<CategoryInfoDO> result = allCategories.stream()
            .map(cat -> new CategoryInfoDO(cat.getName(), cat.getId()))  // 👈 传入 name 和 id
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // @GetMapping("/article-categories")
    // public ResponseEntity<List<BlogCategoryDO>> getCategories() {
    //         return ResponseEntity.ok(blogCategoryService.selectAllCategories());
    //     }
}
    