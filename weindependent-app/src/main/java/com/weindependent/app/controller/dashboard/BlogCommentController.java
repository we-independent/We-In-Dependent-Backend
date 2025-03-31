package com.weindependent.app.controller.dashboard;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.service.IBlogCommentService;


/**
 * 博客评论Controller
 * 
 * @author christina
 * 2025-03-23
 */
@Tag(name = "博客评论管理")
@RestController
@RequestMapping("/dashboard/comment")
public class BlogCommentController
{
    private final IBlogCommentService blogCommentService;

    public BlogCommentController(IBlogCommentService blogCommentService) {
        this.blogCommentService = blogCommentService;
    }

    /**
     * 查询博客评论列表
     */
    @SignatureAuth
    @Operation(summary = "查询博客评论列表")
    @GetMapping("/list")
    public PageInfo<BlogCommentDO> list(@RequestBody Map<String, Object> requestMap)
    {
        int pageNum = (int) requestMap.get("pageNum");
        int pageSize = (int) requestMap.get("pageSize");
        JSONObject jsonObject = JSON.parseObject((String) requestMap.get("data"));
        BlogCommentDO blogComment =  jsonObject.toJavaObject(BlogCommentDO.class);
        return  blogCommentService.selectBlogCommentList(blogComment, pageNum, pageSize);
    }

    /**
     * 查询博客评论详细信息
     */
    @SignatureAuth
    @Operation(summary = "查询博客评论详细信息")
    @GetMapping(value = "/{id}")
    public  BlogCommentDO getInfo(@PathVariable("id") Integer id)
    {
        return blogCommentService.selectBlogCommentById(id);
    }

    /**
     * 新增博客评论
     */
    @SignatureAuth
    @Operation(summary = "新增博客评论")
    @PostMapping
    public boolean add(@RequestBody BlogCommentDO blogComment)
    {
        return blogCommentService.insertBlogComment(blogComment) > 0;
    }

    /**
     * 修改博客评论
     */
    @SignatureAuth
    @Operation(summary = "修改博客评论")
    @PutMapping
    public boolean edit(@RequestBody BlogCommentDO blogComment)
    {
        return blogCommentService.updateBlogComment(blogComment) > 0;
    }

    /**
     * 删除博客评论
     */
    @SignatureAuth
    @Operation(summary = "删除博客评论")
	@DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids)
    {
        return blogCommentService.deleteBlogCommentByIds(ids) > 0;
    }
}
