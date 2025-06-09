package com.weindependent.app.controller;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.IBlogArticleService;
import com.weindependent.app.service.ISearchSuggestService;
import com.weindependent.app.vo.BlogArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@Tag(name = "博客搜索")
@RestController
@RequestMapping("/api/search")
public class SearchBlogController {
    @Autowired
    private IBlogArticleService blogArticleService;
    @Autowired
    private IBlogArticleListService blogArticleListService;
    @Autowired
    private ISearchSuggestService searchSuggestService;

    @Operation(summary = "Search by content")
    @GetMapping("/content")
    public ResponseEntity<List<BlogArticleVO>> searchByContent(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<BlogArticleVO> result = blogArticleService.searchByContent(keyword);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Search by exact words")
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
    public PageInfo<BlogArticleCardQry> searchKeywords(@RequestBody BlogArticleListQry query, @RequestParam String keywords){
        PageInfo<BlogArticleCardQry> result = blogArticleListService.searchByKeywords(query, keywords);

        System.out.println("Full query object = " + query);

        if (result.getList() == null || result.getList().isEmpty()) {
            throw new ResponseException(ErrorCode.BLOG_NOT_EXIST.getCode(), "No articles found under this condition");
        }
        return result;
    }

    @Operation(summary = "Suggest similar keywords for input")
    @GetMapping("/suggest")
    public ResponseEntity<?> suggest(@RequestParam("query") String query) {
        try {
            List<String> suggestions = searchSuggestService.suggestKeywords(query);

            Map<String, Object> response = new HashMap<>();
            response.put("original", query);
            response.put("suggestions", suggestions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error while suggesting keywords", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}