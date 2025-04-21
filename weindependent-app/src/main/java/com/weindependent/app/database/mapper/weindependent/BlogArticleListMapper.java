package com.weindependent.app.database.mapper.weindependent;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogArticleListQry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 博客文章List Mapper 接口
 * 
 * 定义数据库查询方法
 * 
 * @author Hurely
 *    2025-04-5
 */
@Mapper
public interface BlogArticleListMapper {

    /**
     * 根据查询参数查询博客文章列表
     * 
     * @param query 查询参数对象
     * @return 博客文章 DO 集合
     */
    List<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry query);

    /**
     * 根据主键查询单个博客文章
     * 
     * @param id 博客文章主键
     * @return 单个博客文章 DO 对象
     */
    BlogArticleListDO selectBlogArticleById(Integer id);
        /**
     * 根据主键查询单个博客文章comments,tag,bannerimage url
     * 
     * @param id 博客文章主键
     * @return 单个博客文章评论 DO 对象
     */
    List<BlogCommentDO> selectCommentsByArticleId(@Param("articleId") Integer articleId);
    List<String> selectTagsByArticleId(@Param("articleId") Integer articleId);
    String selectBannerImageUrlById(@Param("imgId") Integer imgId);
}
