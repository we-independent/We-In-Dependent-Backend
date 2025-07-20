package com.weindependent.app.controller;

import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.service.IBlogCommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class BlogCommentController {

    private final IBlogCommentService blogCommentService;

    public BlogCommentController(IBlogCommentService blogCommentService) {
        this.blogCommentService = blogCommentService;
    }


    /**
     * 新增博客评论
     */
    @Operation(summary = "新增博客评论")
    @PostMapping
    public boolean add(@RequestBody BlogCommentDO blogComment) {
        return blogCommentService.insertBlogComment(blogComment) > 0;
    }

    /**
     * 修改博客评论
     */
    @Operation(summary = "修改博客评论")
    @PutMapping("/{id}")
    public boolean edit(@PathVariable Integer id, @RequestBody BlogCommentDO blogComment) {
        blogComment.setId(id);
        return blogCommentService.updateBlogComment(blogComment) > 0;
    }

//    /**
//     * 删除博客评论
//     */
//    @SignatureAuth
//    @Operation(summary = "删除博客评论")
//    @DeleteMapping("/{id}")
//    public boolean remove(@PathVariable Integer id) {
//        return blogCommentService.deleteBlogCommentById(id) > 0;
//    }

    /**
     * 删除博客评论
     */
    @Operation(summary = "删除博客评论")
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids) {
        return blogCommentService.deleteBlogCommentByIds(ids) > 0;
    }
}
