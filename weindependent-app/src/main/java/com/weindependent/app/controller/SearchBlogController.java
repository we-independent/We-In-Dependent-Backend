package com.weindependent.app.controller;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.enums.CategoryEnum;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.IBlogArticleService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.vo.BlogArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "博客搜索")
@Slf4j
@RestController
@RequestMapping("/api/search")
public class SearchBlogController {
    @Autowired
    private IBlogArticleService blogArticleService;
    @Autowired
    private IBlogArticleListService blogArticleListService;

    @Operation(summary = "Search by content")
    @SignatureAuth
    @GetMapping("/content")
    public ResponseEntity<List<BlogArticleVO>> searchByContent(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<BlogArticleVO> result = blogArticleService.searchByContent(keyword);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Search by exact words")
    @SignatureAuth
    @GetMapping("/exact-keywords")
    public ResponseEntity<List<BlogArticleVO>> searchByExactKeywords(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<BlogArticleVO> result = blogArticleService.searchByExactKeywords(keyword);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Search by keywords")
    @PostMapping("/keywords")
    public ResponseEntity<Map<String, Object>> searchKeywords(@RequestBody BlogArticleListQry query, @RequestParam String keywords){
        PageInfo<BlogArticleCardQry> pageInfo = blogArticleListService.searchByKeywords(query, keywords);
        
        System.out.println("Full query object = " + query);

        if (pageInfo.getList() == null || pageInfo.getList().isEmpty()) {
            throw new ResponseException(ErrorCode.BLOG_NOT_EXIST.getCode(), "No articles found under this condition");
        }

        // // 获取所有文章ID，批量查询编辑推荐状态和收藏次数
        // List<Integer> articleIds = pageInfo.getList().stream()
        //         .map(BlogArticleListDO::getId)
        //         .collect(Collectors.toList());

        // Map<Integer, Boolean> editorsPickMap = editorsPickService.getEditorsPickMapByArticleIds(articleIds);

        // // 将 DO 转换为前端 BlogCard DTO
        // List<BlogArticleCardQry> resultList = pageInfo.getList().stream().map(article -> {
        //     BlogArticleCardQry dto = new BlogArticleCardQry();
        //     dto.setId(article.getId());
        //     dto.setTitle(article.getTitle());
        //     // update_time 作为 eventime 返回前端
        //     dto.setTime(article.getUpdateTime());
            
        //     // 计算阅读时长：假设每分钟 200 字（常见阅读速度）
        //     int wordCount = article.getContent() != null ? article.getContent().trim().split("\\s+").length : 0;
        //     dto.setReadingTime(Math.min(30, Math.max(1, (int)Math.ceil(wordCount / 200.0))) + " min");

        //     // 图片 URL 和链接可根据实际情况替换，目前为固定值
        //     dto.setImageUrl(blogArticleMapper.selectBannerImageUrlById(article.getBannerImgId()));
        //     // category 直接使用 db 中存储的值，这里转换为字符串；前端可以进一步处理
        //     dto.setCategory(String.valueOf(article.getCategoryId()));
        //     dto.setCategoryName(CategoryEnum.getNameByCode(dto.getCategory()));
        //     dto.setEditorsPick(editorsPickMap.getOrDefault(article.getId(), false));
        //     return dto;
        // }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "success");
        response.put("data", PageInfoUtil.wrapPageData(pageInfo.getList(), pageInfo));
        return ResponseEntity.ok(response);
    }
}
