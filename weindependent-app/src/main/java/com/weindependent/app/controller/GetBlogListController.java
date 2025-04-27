package com.weindependent.app.controller;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.database.mapper.weindependent.BlogArticleListMapper;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.dto.BlogArticleSinglePageQry;
import com.weindependent.app.enums.CategoryEnum;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.IBlogArticleService;
import com.weindependent.app.service.EditorPickService;
import com.weindependent.app.service.SavedCountService;
import com.weindependent.app.service.TagService;
import com.weindependent.app.utils.PageInfoUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "博客文章 Article List 获取")
@RestController
@RequestMapping("/api")
public class GetBlogListController {

    @Autowired
    private IBlogArticleListService blogArticleListService;
    
    private final BlogArticleListMapper blogArticleMapper;
    public GetBlogListController(BlogArticleListMapper blogArticleMapper) {
        this.blogArticleMapper = blogArticleMapper;
    }
    @Autowired
    private EditorPickService editorsPickService;
    
    @Autowired
    private SavedCountService savedCountService;
    
    @Autowired
    private IBlogArticleService blogarticleService;

    @Autowired
    private TagService tagService;

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
            throw new ResponseException(ErrorCode.BLOG_NOT_EXIST.getCode(), "No articles found under this condition");
        }

        // 获取所有文章ID，批量查询编辑推荐状态和收藏次数
        List<Integer> articleIds = pageInfo.getList().stream()
                .map(BlogArticleListDO::getId)
                .collect(Collectors.toList());

        Map<Integer, Boolean> editorsPickMap = editorsPickService.getEditorsPickMapByArticleIds(articleIds);
        Map<Integer, Integer> savedCountMap;
        try {
            savedCountMap = savedCountService.getSavedCountMapByArticleIds(articleIds);
        } catch (Exception e) {
            log.warn("获取收藏数失败，使用默认值 0", e);
            savedCountMap = articleIds.stream().collect(Collectors.toMap(id -> id, id -> 0));
        }

        final Map<Integer, Integer> finalSavedCountMap = savedCountMap;
        log.info("当前页面文章 ID: " + articleIds);
        log.info("收藏数 map: " + savedCountMap);

        // 将 DO 转换为前端 BlogCard DTO
        List<BlogArticleCardQry> resultList = pageInfo.getList().stream().map(article -> {
            BlogArticleCardQry dto = new BlogArticleCardQry();
            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            // update_time 作为 eventime 返回前端
            dto.setTime(article.getUpdateTime());
            
            // 计算阅读时长：假设每分钟 200 字（常见阅读速度）
            int wordCount = article.getContent() != null ? article.getContent().trim().split("\\s+").length : 0;
            dto.setReadingTime(Math.min(30, Math.max(1, (int)Math.ceil(wordCount / 200.0))) + " min");

            // 图片 URL 和链接可根据实际情况替换，目前为固定值
            dto.setImageUrl(blogArticleMapper.selectBannerImageUrlById(article.getBannerImgId()));
            // category 直接使用 db 中存储的值，这里转换为字符串；前端可以进一步处理
            dto.setCategory(String.valueOf(article.getCategoryId()));
            dto.setCategoryName(CategoryEnum.getNameByCode(dto.getCategory()));
            dto.setEditorsPick(editorsPickMap.getOrDefault(article.getId(), false));
            dto.setSavedCount(finalSavedCountMap.getOrDefault(article.getId(), 0));
            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "success");
        response.put("data", PageInfoUtil.wrapPageData(resultList, pageInfo));
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "获取单独博客文章, by id from blogcard when click title")
    @GetMapping("/article/{id}")

    public BlogArticleSinglePageQry getSingleArticle(
        @PathVariable("id") Integer id,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        BlogArticleSinglePageQry articleQry = blogArticleListService.getArticleDetailById(id, pageNum, pageSize);
        if (articleQry == null){
            // 抛出业务异常，让 GlobalExceptionResolver 自动包装
            // log.info(String.valueOf(ErrorCode.BLOG_NOT_EXIST.getCode()));
            throw new ResponseException(ErrorCode.BLOG_NOT_EXIST.getCode(), "文章不存在");
        }
        return articleQry; // 直接返回业务数据，不包装
    }

    @Operation(summary = "获取related文章card")
    @GetMapping("/article/related")
    public Object getRelatedArticles(@RequestParam int articleId, @RequestParam(defaultValue = "3") int limit){
        try {
            //先获取当前article的Category和taglist
            BlogArticleDO currentArticle = blogarticleService.selectBlogArticleById(articleId);
            if(currentArticle == null){
                throw new ResponseException(ErrorCode.BLOG_NOT_EXIST.getCode(), "文章不存在");
            }
            Integer categoryId = currentArticle.getCategoryId();
            List<TagDO> tagList = tagService.getTagsByArticleId(articleId);
            List<Integer> tagIdList = tagList.stream().map(TagDO::getId).collect(Collectors.toList());
            log.info("tagIdList = " + tagIdList);

            //找出所有同分类同tag的文章
            List<BlogArticleCardQry> candidates = blogArticleListService.getArticlesByCategoryOrTagsExcludeSelf(categoryId, tagIdList, articleId);
            log.info("category: {}, tags: {}, candidates: {}", categoryId, tagIdList, candidates);
            //从候选文章中随机挑选三篇文章显示
            if (candidates == null || candidates.isEmpty()) {
                return Collections.emptyList();
            }
            Collections.shuffle(candidates);
            return candidates.stream().limit(limit).collect(Collectors.toList());

        }catch (Exception e) {
            log.error("Fail to Get Related Articles", e);
            throw new ResponseException(ErrorCode.RELATED_ARTICLE_FETCH_FAILED.getCode(), "Fail to Get Related Articles");
        }
    }

}
