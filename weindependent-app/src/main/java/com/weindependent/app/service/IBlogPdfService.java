package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.dto.BlogPdfQry;

/**
 * 博客文章pdfService接口
 * 
 * @author christina
 *    2025-03-23
 */
public interface IBlogPdfService {
    /**
     * 查询博客文章pdf
     *
     * @param id 博客文章pdf主键
     * @return 博客文章pdf
     */
    BlogPdfDO selectBlogPdfById(Integer id);

    /**
     * 查询博客文章pdf列表
     *
     * @param blogPdfQry 博客文章pdf查询模板
     * @return 博客文章pdf集合
     */
    PageInfo<BlogPdfDO> selectBlogPdfList(BlogPdfQry blogPdfQry);

    /**
     * 新增博客文章pdf
     *
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    int insertBlogPdf(BlogPdfDO blogPdf);

    /**
     * 修改博客文章pdf
     *
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    int updateBlogPdf(BlogPdfDO blogPdf);

    /**
     * 批量删除博客文章pdf
     *
     * @param ids 需要删除的博客文章pdf主键集合
     * @return 结果
     */
    int deleteBlogPdfByIds(Integer[] ids, int updateUserId);

    /**
     * 删除博客文章pdf信息
     *
     * @param id 博客文章pdf主键
     * @return 结果
     */
    int deleteBlogPdfById(Integer id);

    /**
     * 根据博客 ID 生成 PDF 文件内容（字节流形式）
     *
     * @param id 博客文章的主键 ID
     * @return 生成的 PDF 文件内容（byte 数组）
     * @author Hurely
     * @since 2025-04-2
     */
    byte[] generatePdf(Integer id);
}
