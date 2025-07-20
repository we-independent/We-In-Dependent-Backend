package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogCommentQry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.service.IDashboardBlogCommentService;


/**
 * 博客评论Controller
 * 
 * @author christina
 * 2025-03-23
 */
@Tag(name = "博客评论管理")
@RestController
@RequestMapping("api/dashboard/comment")
public class DashboardBlogCommentController {
    private final IDashboardBlogCommentService blogCommentService;

    public DashboardBlogCommentController(IDashboardBlogCommentService blogCommentService) {
        this.blogCommentService = blogCommentService;
    }

    /**
     * 查询博客评论列表
     */
    @SaCheckRole("admin")
    @Operation(summary = "查询博客评论列表")
    @PostMapping("/list")
    public PageInfo<BlogCommentDO> list(@RequestBody BlogCommentQry blogCommentQry) {


        return blogCommentService.selectBlogCommentList(blogCommentQry);

    }

    /**
     * 删除博客评论
     */
    @SaCheckRole("admin")
    @Operation(summary = "删除博客评论")
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids) {

        return blogCommentService.deleteBlogCommentByIds(ids) > 0;
    }
}
