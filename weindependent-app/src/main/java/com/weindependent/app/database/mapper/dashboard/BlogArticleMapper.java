package com.weindependent.app.database.mapper.dashboard;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客文章Mapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 * @date 2025-03-23
 */
@Mapper
public interface BlogArticleMapper 
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
    public List<BlogArticleDO> selectBlogArticleList(BlogArticleDO blogArticle);

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
     * 删除博客文章 , 软删除
     * 
     * @param id 博客文章主键
     * @return 结果
     */
    public int deleteBlogArticleById(Integer id);

    /**
     * 批量删除博客文章 , 软删除
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBlogArticleByIds(Integer[] ids);
}
