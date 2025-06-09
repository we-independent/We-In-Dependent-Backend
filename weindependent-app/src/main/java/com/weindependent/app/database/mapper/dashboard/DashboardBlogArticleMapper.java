package com.weindependent.app.database.mapper.dashboard;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.vo.BlogArticleEditVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 博客文章Mapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 *
 * @author christina
 * 2025-03-23
 */
@Mapper
public interface DashboardBlogArticleMapper {
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
     * @param blogArticle 博客文章
     * @return 博客文章集合
     */
    List<BlogArticleDO> selectBlogArticleList(BlogArticleDO blogArticle);

    /**
     * 新增博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
    int insertBlogArticle(BlogArticleDO blogArticle);

    /**
     * 修改博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
    int updateBlogArticle(BlogArticleDO blogArticle);

    int updateBlogArticleWithBanner(BlogArticleDO blogArticleDO); //Hurely

    /**
     * 删除博客文章 , 软删除
     *
     * @param id 博客文章主键
     * @return 结果
     */
    int deleteBlogArticleById(Integer id);

    /**
     * 批量删除博客文章 , 软删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBlogArticleByIds(List<Integer> ids, int updateUserId);

    int deleteBlogArticleWithRelations(@Param("ids") List<Integer> ids, @Param("updateUserId") Integer updateUserId); //Hurely


    /**
     * 全文搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    List<BlogArticleDO> searchByContent(String keyword);


    /**
     * 标题关键词搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    List<BlogArticleDO> searchByExactKeywords(String keyword);

    /**
     * 标题和id关键词搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    List<BlogArticleDO> searchByIdAndTitle(String keyword);

    boolean existsById(int id);

    BlogArticleEditVO selectBlogArticleEditVOById(Integer id);
}
