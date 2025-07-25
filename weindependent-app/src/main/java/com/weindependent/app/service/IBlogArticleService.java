package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.dto.BlogArticleEditQry;
import com.weindependent.app.vo.BlogArticleEditVO;
import com.weindependent.app.vo.BlogArticleVO;
import com.weindependent.app.dto.BlogArticleQry;

import org.springframework.web.multipart.MultipartFile;


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
     * 查询博客文章
     *
     * @param id 博客文章主键
     * @return 博客文章
     */
    BlogArticleEditVO selectBlogArticleByIdForEdit(Integer id);

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
    int insertBlogArticle(BlogArticleEditQry blogArticle);

    /**
     * 新增博客图片
     *
     * @param fileUploadQry 博客图片
     * @return 结果
     */
    ImageDO insertBlogBanner(MultipartFile fileUploadQry);

    /**
     * 修改博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
     int updateBlogArticle(BlogArticleEditQry blogArticle);

    /**
     * 批量删除博客文章
     *
     * @param ids 需要删除的博客文章主键集合
     * @return 结果
     */
    int deleteBlogArticleByIds(List<Integer> ids);

    /**
     * 删除博客文章信息
     *
     * @param id 博客文章主键
     * @return 结果
     */
    int deleteBlogArticleById(List<Integer> id);

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


    List<BlogArticleDO>  searchByIdAndTitle(String keyword);
}
