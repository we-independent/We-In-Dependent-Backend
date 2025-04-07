package com.weindependent.app.database.mapper.weindependent;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogArticleListDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 博客文章ListMapper接口
 * 
 * 
 * @author Hurely
 *    2025-04-5
 */
@Mapper
public interface BlogArticleListMapper 
{
    /**
     * 查询博客文章
     * 
     * @param id 博客文章主键
     * @return 博客文章
     */
    public BlogArticleListDO selectBlogArticleById(Integer id);

    /**
     * 查询博客文章列表
     * 
     * @param blogArticleListDO 博客文章
     * @return 博客文章集合
     */
    public List<BlogArticleListDO> selectBlogArticleList(BlogArticleListDO blogArticleDO);

    // /**
    //  * 新增博客文章
    //  * 
    //  * @param blogArticle 博客文章
    //  * @return 结果
    //  */
    // public int insertBlogArticle(BlogArticleListDO blogArticle);

    // /**
    //  * 修改博客文章
    //  * 
    //  * @param blogArticle 博客文章
    //  * @return 结果
    //  */
    // public int updateBlogArticle(BlogArticleListDO blogArticle);

    // /**
    //  * 删除博客文章 , 软删除
    //  * 
    //  * @param id 博客文章主键
    //  * @return 结果
    //  */
    // public int deleteBlogArticleById(Integer id);

    // /**
    //  * 批量删除博客文章 , 软删除
    //  * 
    //  * @param ids 需要删除的数据主键集合
    //  * @return 结果
    //  */
    // public int deleteBlogArticleByIds(Integer[] ids);
}
