package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.dto.BlogArticleAddQry;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.vo.BlogArticleEditVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import com.weindependent.app.service.IBlogArticleService;
import org.springframework.web.multipart.MultipartFile;


/**
 * 博客文章Controller
 * 
 * @author christina
 *    2025-03-23
 */
@Tag(name = "博客文章管理")
@RestController
@RequestMapping("api/dashboard/blog")
public class DashboardBlogArticleController {
    private final IBlogArticleService blogArticleService;

    public DashboardBlogArticleController(IBlogArticleService blogArticleService) {
        this.blogArticleService = blogArticleService;
    }

    /**
     * 查询博客文章列表
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "查询博客文章列表")
    @PostMapping("/list")
    public PageInfo<BlogArticleDO> list(@RequestBody BlogArticleQry blogArticleQry) {
        return blogArticleService.selectBlogArticleList(blogArticleQry);
    }

    /**
     * 查询博客文章详细信息
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "查询博客文章详细信息")
    @GetMapping(value = "/{id}")
    public BlogArticleEditVO getInfo(@PathVariable("id") Integer id) {
        return blogArticleService.selectBlogArticleByIdForEdit(id);
    }

    /**
     * 新增博客文章
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "新增博客文章")
    @PostMapping
    public boolean add(@RequestBody BlogArticleAddQry blogArticle) {
        int userId = StpUtil.getLoginIdAsInt();
        return blogArticleService.insertBlogArticle(blogArticle,userId) > 0;
    }

    /**
     * 新增博客图片
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "新增博客banner图片")
    @PostMapping(value = "/banner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageDO addBlogBanner(@RequestParam("file") MultipartFile file) {
        return blogArticleService.insertBlogBanner(file);
    }

    /**
     * 修改博客文章
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "修改博客文章")
    @PutMapping
    public boolean edit(@RequestBody BlogArticleAddQry blogArticle) {
        int userId = StpUtil.getLoginIdAsInt();

        return blogArticleService.updateBlogArticle(blogArticle, userId) > 0;
    }

    /**
     * 删除博客文章
     */
    @SignatureAuth
    @SaCheckRole("admin")
    @Operation(summary = "删除博客文章")
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids) {
        int updateUserId = StpUtil.getLoginIdAsInt();
        return blogArticleService.deleteBlogArticleByIds(ids, updateUserId) > 0;
    }
}
