package com.weindependent.app.controller.dashboard;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


import com.weindependent.app.service.IBlogArticleService;



/**
 * 博客文章Controller
 * 
 * @author christina
 * @date 2025-03-23
 */
@Tag(name = "博客文章管理")
@RestController
@RequestMapping("/dashboard/blog")
public class BlogArticleController
{
    private final IBlogArticleService blogArticleService;

    public BlogArticleController(IBlogArticleService blogArticleService){
        this.blogArticleService=blogArticleService;
    }

    /**
     * 查询博客文章列表
     */
    @SignatureAuth
    @Operation(summary = "查询博客文章列表")
    @GetMapping("/list")
    public PageInfo<BlogArticleDO> list(@RequestBody Map<String, Object> requestMap)
    {
        int pageNum = (int) requestMap.get("pageNum");
        int pageSize = (int) requestMap.get("pageSize");
        JSONObject jsonObject = JSON.parseObject((String) requestMap.get("data"));
        BlogArticleDO blogArticle = jsonObject.toJavaObject(BlogArticleDO.class);
        return blogArticleService.selectBlogArticleList(blogArticle, pageNum, pageSize);
    }

    /**
     * 查询博客文章详细信息
     */
    @SignatureAuth
    @Operation(summary = "查询博客文章详细信息")
    @GetMapping(value = "/{id}")
    public BlogArticleDO getInfo(@PathVariable("id") Integer id)
    {
        return blogArticleService.selectBlogArticleById(id);
    }

    /**
     * 新增博客文章
     */
    @SignatureAuth
    @Operation(summary = "新增博客文章")
    @PostMapping
    public boolean add(@RequestBody BlogArticleDO blogArticle)
    {
        return blogArticleService.insertBlogArticle(blogArticle) > 0;
    }

    /**
     * 修改博客文章
     */
    @SignatureAuth
    @Operation(summary = "修改博客文章")
    @PutMapping
    public boolean edit(@RequestBody BlogArticleDO blogArticle)
    {
        return blogArticleService.updateBlogArticle(blogArticle) > 0;
    }

    /**
     * 删除博客文章
     */
    @SignatureAuth
    @Operation(summary = "查询博客文章列表")
	@DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids)
    {
        return blogArticleService.deleteBlogArticleByIds(ids) > 0;
    }
}
