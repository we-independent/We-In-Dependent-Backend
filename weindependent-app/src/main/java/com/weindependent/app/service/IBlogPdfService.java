package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogPdfDO;

/**
 * 博客文章pdfService接口
 * 
 * @author christina
 * @date 2025-03-23
 */
public interface IBlogPdfService 
{
    /**
     * 查询博客文章pdf
     * 
     * @param id 博客文章pdf主键
     * @return 博客文章pdf
     */
    public BlogPdfDO selectBlogPdfById(Integer id);

    /**
     * 查询博客文章pdf列表
     * 
     * @param blogPdf 博客文章pdf
     * @return 博客文章pdf集合
     */
    public PageInfo<BlogPdfDO> selectBlogPdfList(BlogPdfDO blogPdf, int pageNum, int pageSize);

    /**
     * 新增博客文章pdf
     * 
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    public int insertBlogPdf(BlogPdfDO blogPdf);

    /**
     * 修改博客文章pdf
     * 
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    public int updateBlogPdf(BlogPdfDO blogPdf);

    /**
     * 批量删除博客文章pdf
     * 
     * @param ids 需要删除的博客文章pdf主键集合
     * @return 结果
     */
    public int deleteBlogPdfByIds(Integer[] ids);

    /**
     * 删除博客文章pdf信息
     * 
     * @param id 博客文章pdf主键
     * @return 结果
     */
    public int deleteBlogPdfById(Integer id);
}
