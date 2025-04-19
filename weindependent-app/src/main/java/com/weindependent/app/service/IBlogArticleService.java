package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.vo.BlogArticleVO;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.dto.FileUploadQry;
import com.weindependent.app.vo.UploadedFileVO;


/**
 * 博客文章Service接口
 * 
 * @author christina
 *    2025-03-23
 */
public interface IBlogArticleService {
    /**
     * 查询博客文章
     *
     * @param id 博客文章主键
     * @return 博客文章
     */
    BlogArticleDO selectBlogArticleById(Integer id);

    /**
     * 查询博客文章列表
     *
     * @param blogArticleQry 博客文章查询模板
     * @return 博客文章集合
     */
    PageInfo<BlogArticleDO> selectBlogArticleList(BlogArticleQry blogArticleQry);

    /**
     * 新增博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
    int insertBlogArticle(BlogArticleDO blogArticle);

    /**
     * 新增博客图片
     *
     * @param fileUploadQry 博客图片
     * @return 结果
     */
    UploadedFileVO insertBlogBanner(FileUploadQry fileUploadQry);

    /**
     * 修改博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
    int updateBlogArticle(BlogArticleDO blogArticle);

    /**
     * 批量删除博客文章
     *
     * @param ids 需要删除的博客文章主键集合
     * @return 结果
     */
    int deleteBlogArticleByIds(Integer[] ids, int updateUserId);

    /**
     * 删除博客文章信息
     *
     * @param id 博客文章主键
     * @return 结果
     */
    int deleteBlogArticleById(Integer id);

    /**
     * 全文搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    List<BlogArticleVO> searchByContent(String keyword);

    /**
     * 标题关键词搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    List<BlogArticleVO> searchByExactKeywords(String keyword);
}
