package com.weindependent.app.database.mapper.dashboard;

import java.util.List;

import com.weindependent.app.database.dataobject.BlogPdfDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客文章pdfMapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Mapper
public interface DashboardBlogPdfMapper {
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
     * @param blogPdf 博客文章pdf
     * @return 博客文章pdf集合
     */
    List<BlogPdfDO> selectBlogPdfList(BlogPdfDO blogPdf);

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
     * 删除博客文章pdf信息，, 软删除
     *
     * @param id 博客文章pdf主键
     * @return 结果
     */
    int deleteBlogPdfById(Integer id);

    /**
     * 批量删除博客文章pdf信息，, 软删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBlogPdfByIds(Integer[] ids, int updateUserId);
}
