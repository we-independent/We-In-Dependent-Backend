package com.weindependent.app.controller;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.dto.BlogArticleSinglePageQry;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.EditorPickService;
import com.weindependent.app.service.SavedCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "博客文章 Article List 获取")
@RestController
@RequestMapping("/")
public class GetBlogListController {

    @Autowired
    private IBlogArticleListService blogArticleListService;
    
    @Autowired
    private EditorPickService editorsPickService;
    
    @Autowired
    private SavedCountService savedCountService;

    /**
     * 获取文章列表接口：
     * - 如果传入 categoryId 则按分类筛选，否则返回所有文章
     * - 支持分页与排序（orderBy: "latest" 或 "most_saved"）
     * - 返回数据格式按照 BlogArticleCardDTO 封装，front-end 用于 BlogCard 显示
     */
    @Operation(summary = "获取文章列表，支持分页、排序及分类筛选")
    @PostMapping("/articles")
    public ResponseEntity<Map<String, Object>> getArticles(@RequestBody BlogArticleListQry query) {
        PageInfo<BlogArticleListDO> pageInfo = blogArticleListService.selectBlogArticleList(query);
        
        if (pageInfo.getList() == null || pageInfo.getList().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1001);
            response.put("msg", "No articles found under this condition");
            response.put("data", Collections.emptyList());
            return ResponseEntity.ok(response);
        }

        // 获取所有文章ID，批量查询编辑推荐状态和收藏次数
        List<Integer> articleIds = pageInfo.getList().stream()
                .map(BlogArticleListDO::getId)
                .collect(Collectors.toList());

        Map<Integer, Boolean> editorsPickMap = editorsPickService.getEditorsPickMapByArticleIds(articleIds);
        Map<Integer, Integer> tempMap;
        try {
            tempMap = savedCountService.getSavedCountMapByArticleIds(articleIds);
        } catch (Exception e) {
            tempMap = new HashMap<>();
            for (Integer id : articleIds) {
                tempMap.put(id, 0);
            }
        }
        final Map<Integer, Integer> finalSavedCountMap = tempMap;
        System.out.println("当前页面文章 ID: " + articleIds);
        System.out.println("收藏数 map: " + tempMap);
        
        // 将 DO 转换为前端 BlogCard DTO
        List<BlogArticleCardQry> resultList = pageInfo.getList().stream().map(article -> {
            BlogArticleCardQry dto = new BlogArticleCardQry();
            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            // update_time 作为 eventime 返回前端
            dto.setTime(article.getUpdateTime());
            
            // 计算阅读时长：假设每分钟 200 字（常见阅读速度）
            int wordCount = article.getContent() != null ? article.getContent().length() : 0;
            dto.setReadingTime((int)Math.ceil(wordCount / 200.0) + " min");

            // 图片 URL 和链接可根据实际情况替换，目前为固定值
            dto.setImageUrl("BlogArticleImage1");
            dto.setUrl("/blogs/visa-policy/policy-changes-2025");
            
            // category 直接使用 db 中存储的值，这里转换为字符串；前端可以进一步处理
            dto.setCategory(String.valueOf(article.getCategoryId()));
            dto.setEditorsPick(editorsPickMap.getOrDefault(article.getId(), false));
            dto.setSavedCount(finalSavedCountMap.getOrDefault(article.getId(), 0));
            return dto;
        }).collect(Collectors.toList());

        // 构造分页返回数据
        Map<String, Object> dataWrapper = new HashMap<>();
        dataWrapper.put("list", resultList);
        dataWrapper.put("pageNum", pageInfo.getPageNum());
        dataWrapper.put("pageSize", pageInfo.getPageSize());
        dataWrapper.put("total", pageInfo.getTotal());
        dataWrapper.put("pages", pageInfo.getPages());

        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "success");
        response.put("data", dataWrapper);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "获取单独博客文章, by id from blogcard when click title")
    @GetMapping("/article/{id}")
    public ResponseEntity<?> getSingleArticle(    @PathVariable("id") Integer id,
    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize){
        // 获取文章详细信息
        BlogArticleSinglePageQry articleQry = blogArticleListService.getArticleDetailById(id, pageNum, pageSize);
        if (articleQry == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article not found");
        }
        return ResponseEntity.ok(articleQry);
    }


}
