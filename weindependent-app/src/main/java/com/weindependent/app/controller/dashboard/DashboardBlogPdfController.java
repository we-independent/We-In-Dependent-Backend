package com.weindependent.app.controller.dashboard;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.dto.BlogPdfQry;
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

import com.weindependent.app.service.IBlogPdfService;



/**
 * 博客文章pdfController
 * 
 * @author christina
 *    2025-03-23
 */
@Tag(name = "博客文章pdf管理")
@RestController
@RequestMapping("api/dashboard/pdf")
public class DashboardBlogPdfController
{
    private final IBlogPdfService blogPdfService;

    public DashboardBlogPdfController(IBlogPdfService blogPdfService) {
        this.blogPdfService = blogPdfService;
    }

    /**
     * 查询博客文章pdf列表
     */
    @SignatureAuth
    @Operation(summary = "查询博客文章pdf列表")
    @GetMapping("/list")
    public PageInfo<BlogPdfDO> list(@RequestBody BlogPdfQry blogPdfQry)
    {
        return blogPdfService.selectBlogPdfList(blogPdfQry);
    }

    /**
     * 查询博客文章pdf详细信息
     */
    @SignatureAuth
    @Operation(summary = "查询博客文章pdf详细信息")
    @GetMapping(value = "/{id}")
    public BlogPdfDO getInfo(@PathVariable("id") Integer id)
    {
        return blogPdfService.selectBlogPdfById(id);
    }

    /**
     * 新增博客文章pdf
     */
    @SignatureAuth
    @Operation(summary = "新增博客文章pdf")
    @PostMapping
    public boolean add(@RequestBody BlogPdfDO blogPdf)
    {
        return blogPdfService.insertBlogPdf(blogPdf) > 0;
    }

    /**
     * 修改博客文章pdf
     */
    @SignatureAuth
    @Operation(summary = "修改博客文章pdf")
    @PutMapping
    public boolean edit(@RequestBody BlogPdfDO blogPdf)
    {
        return blogPdfService.updateBlogPdf(blogPdf) > 0;
    }

    /**
     * 删除博客文章pdf
     */
    @SignatureAuth
    @Operation(summary = "删除博客文章pdf")
	@DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids)
    {
        return blogPdfService.deleteBlogPdfByIds(ids) > 0;
    }
}
