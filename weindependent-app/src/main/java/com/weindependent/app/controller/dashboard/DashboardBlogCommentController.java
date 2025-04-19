package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogCommentQry;
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
@RequestMapping("api/dashboard/comment")
public class DashboardBlogCommentController {
    private final IBlogCommentService blogCommentService;

    public DashboardBlogCommentController(IBlogCommentService blogCommentService) {

        this.blogCommentService = blogCommentService;
    }

    /**
     * 查询博客评论列表
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "查询博客评论列表")
    @PostMapping("/list")
    public PageInfo<BlogCommentDO> list(@RequestBody BlogCommentQry blogCommentQry) {


        return blogCommentService.selectBlogCommentList(blogCommentQry);

    }

    /**
     * 查询博客评论详细信息
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "查询博客评论详细信息")
    @GetMapping(value = "/{id}")
    public BlogCommentDO getInfo(@PathVariable("id") Integer id) {
        return blogCommentService.selectBlogCommentById(id);
    }

    /**
     * 新增博客评论
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "新增博客评论")
    @PostMapping
    public boolean add(@RequestBody BlogCommentDO blogComment) {
        int userId = StpUtil.getLoginIdAsInt();
        blogComment.setCreateUserId(userId);
        blogComment.setUpdateUserId(userId);
        return blogCommentService.insertBlogComment(blogComment) > 0;
    }

    /**
     * 修改博客评论
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "修改博客评论")
    @PutMapping
    public boolean edit(@RequestBody BlogCommentDO blogComment) {
        int userId = StpUtil.getLoginIdAsInt();
        blogComment.setUpdateUserId(userId);
        return blogCommentService.updateBlogComment(blogComment) > 0;
    }

    /**
     * 删除博客评论
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "删除博客评论")
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids) {
        int updateUserId = StpUtil.getLoginIdAsInt();
        return blogCommentService.deleteBlogCommentByIds(ids, updateUserId) > 0;
    }
}
