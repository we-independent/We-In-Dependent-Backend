package com.weindependent.app.controller;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.database.dataobject.BlogCategoryDO;
import com.weindependent.app.database.dataobject.CategoryInfoDO;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.dto.BlogAllArticleQry;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.IBlogArticleCategoryService;
import com.weindependent.app.service.EditorPickService;
import com.weindependent.app.service.SavedCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "博客文章Article List获取")
@RestController
@RequestMapping("/")
public class GetBlogListController {

    @Autowired
    private IBlogArticleListService blogArticleListService;
    
    @Autowired
    private IBlogArticleCategoryService blogCategoryService;

    @Autowired
    private EditorPickService editorsPickService;
    
    @Autowired
    private SavedCountService savedCountService;

    /**
     * 查询博客文章列表，返回结构使用 BlogAllArticleQry（DTO），其中包含前端需要展示的字段
     */
    @Operation(summary = "显示所有Article并默认按update_time desc排序, most_saved数据还没找到在哪，方法已写")
    @PostMapping("/articles/allArticle")
    public ResponseEntity<Map<String, Object>> allArticle(
            @RequestBody BlogAllArticleQry query
    ) {
        // 1. 调用 service 查询分页后的文章列表（返回 BlogArticleListDO 集合）
        PageInfo<BlogArticleListDO> pageInfo = blogArticleListService.selectBlogArticleList(query);
        Map<String, Object> response = new HashMap<>();

        if (pageInfo.getList() == null || pageInfo.getList().isEmpty()) {
            response.put("code", 1001);
            response.put("msg", "No articles found under this condition");
            response.put("data", Collections.emptyList());
            return ResponseEntity.ok(response);
        }

        // ---------------------- 批量查询数据开始 -------------------------
        // 提取所有文章ID，用于查询 editorsPick 和 savedCount 的数据
        List<Integer> articleIds = pageInfo.getList().stream()
                .map(BlogArticleListDO::getId)
                .collect(Collectors.toList());

        // 批量查询 editorsPick 状态（假设返回 Map<Integer, Boolean>）
        Map<Integer, Boolean> editorsPickMap = editorsPickService.getEditorsPickMapByArticleIds(articleIds);
        Map<Integer, Integer> savedCountMap;
        try {
            savedCountMap = savedCountService.getSavedCountMapByArticleIds(articleIds);
        } catch (Exception e) {
            Map<Integer, Integer> fallback = new HashMap<>();
            for (Integer id : articleIds) {
                fallback.put(id, 0);
            }
            savedCountMap = fallback;
        }
        
        // ✅ 创建一个 final 临时变量供 lambda 使用
        final Map<Integer, Integer> finalSavedCountMap = savedCountMap;
        
        // ---------------------- 批量查询数据结束 -------------------------

        // 2. 将 BlogArticleListDO 转换为 BlogAllArticleQry
        List<BlogAllArticleQry> resultList = pageInfo.getList().stream().map(article -> {
            BlogAllArticleQry dto = new BlogAllArticleQry();
            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            // 这里假设 description 为文章的 content，你也可以做其它逻辑处理
            dto.setDescription(article.getContent());

            // 格式化更新时间为字符串，例如 "Feb 05 2025"
            dto.setTime(article.getUpdateTime());

            // 例如作者固定为 "We Independent"，也可以根据具体逻辑处理
            dto.setAuthor("We Independent");

            // 计算阅读时长，假定每分钟200字
            int wordsPerMinute = 200;
            int wordCount = article.getContent() != null ? article.getContent().length() : 0;
            int readingTimeMin = (int) Math.ceil((double) wordCount / wordsPerMinute);
            dto.setReadingTime(readingTimeMin + " min");

            // 图片 URL、文章链接等可以根据具体业务进行封装，这里示例为固定值
            dto.setImageUrl("BlogArticleImage1");
            dto.setUrl("/blogs/visa-policy/policy-changes-2025");
            
            // 分类字段，这里直接将 categoryId 转换为字符串，实际情况可以根据 categoryId 获取分类名称
            dto.setCategory(String.valueOf(article.getCategoryId()));

            // 从批量查询结果获取 editorsPick 状态和 savedCount
            Boolean editorsPick = editorsPickMap.get(article.getId());
            dto.setEditorsPick(editorsPick != null ? editorsPick : false);
            Integer savedCount = finalSavedCountMap.get(article.getId());
            dto.setSavedCount(finalSavedCountMap.getOrDefault(article.getId(), 0));

            // 如果需要添加 trending 字段，可在此处设置

            return dto;
        }).collect(Collectors.toList());

        // 3. 构造分页返回数据（可选：如果前端需要分页信息）
        Map<String, Object> dataWrapper = new HashMap<>();
        dataWrapper.put("list", resultList);
        dataWrapper.put("pageNum", pageInfo.getPageNum());
        dataWrapper.put("pageSize", pageInfo.getPageSize());
        dataWrapper.put("total", pageInfo.getTotal());
        dataWrapper.put("pages", pageInfo.getPages());

        response.put("code", 0);
        response.put("msg", "success");
        response.put("data", dataWrapper);
        return ResponseEntity.ok(response);
    }

    /**
     * 简单格式化时间为字符串，格式例如 "MMM dd yyyy", 如 "Feb 05 2025"
     */
    private String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
        return df.format(date);
    }
    
    // /**
    //  * 通过 Category 查询文章列表接口，未作响应 DTO 转换（保持原样返回分页 PageInfo）
    //  */
    // @SignatureAuth
    // @Operation(summary = "通过Category_id获得所有相关Article并默认按update_time desc排序")
    // @PostMapping("/articles/by-category")
    // public ResponseEntity<Map<String, Object>> articleList(
    //         @RequestBody BlogArticleListQry query
    // ) {
    //     PageInfo<BlogArticleListDO> result = blogArticleListService.selectBlogArticleList(query);
    //     Map<String, Object> response = new HashMap<>();
    //     if (result.getList() == null || result.getList().isEmpty()) {
    //         response.put("code", 1001);
    //         response.put("msg", "No articles found under this condition");
    //         response.put("data", Collections.emptyList());
    //     } else {
    //         response.put("code", 0);
    //         response.put("msg", "success");
    //         response.put("data", result);
    //     }
    //     return ResponseEntity.ok(response);
    // }

    @Operation(summary = "获取所有Blog Category列表")
    @GetMapping("/category-names")
    public ResponseEntity<List<CategoryInfoDO>> getCategoryNames() {
        List<BlogCategoryDO> allCategories = blogCategoryService.selectAllCategories();
        List<CategoryInfoDO> result = allCategories.stream()
                .map(cat -> new CategoryInfoDO(cat.getName(), cat.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
