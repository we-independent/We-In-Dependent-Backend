package com.weindependent.app.controller;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.service.IBlogArticleService;
import com.weindependent.app.vo.BlogArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "博客搜索")
@Slf4j
@RestController
@RequestMapping("/api/search")
public class SearchBlogController {
    @Autowired
    private IBlogArticleService blogArticleService;

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
}
