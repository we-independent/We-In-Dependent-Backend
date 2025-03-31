package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleDO;


/**
 * 博客文章Service接口
 * 
 * @author christina
 * @date 2025-03-23
 */
public interface IBlogArticleService 
{
    /**
     * 查询博客文章
     * 
     * @param id 博客文章主键
     * @return 博客文章
     */
    public BlogArticleDO selectBlogArticleById(Integer id);

    /**
     * 查询博客文章列表
     * 
     * @param blogArticle 博客文章
     * @return 博客文章集合
     */
    public PageInfo<BlogArticleDO> selectBlogArticleList(BlogArticleDO blogArticle,int pageNum,int pageSize);

    /**
     * 新增博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    public int insertBlogArticle(BlogArticleDO blogArticle);

    /**
     * 修改博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    public int updateBlogArticle(BlogArticleDO blogArticle);

    /**
     * 批量删除博客文章
     * 
     * @param ids 需要删除的博客文章主键集合
     * @return 结果
     */
    public int deleteBlogArticleByIds(Integer[] ids);

    /**
     * 删除博客文章信息
     * 
     * @param id 博客文章主键
     * @return 结果
     */
    public int deleteBlogArticleById(Integer id);
}
