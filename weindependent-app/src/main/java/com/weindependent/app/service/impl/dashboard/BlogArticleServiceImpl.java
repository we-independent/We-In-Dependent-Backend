package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.dashboard.BlogArticleMapper;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.utils.PageInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogArticleService;


/**
 * 博客文章Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 * @date 2025-03-23
 */
@Service
public class BlogArticleServiceImpl implements IBlogArticleService 
{
    private final BlogArticleMapper blogArticleMapper;

    public BlogArticleServiceImpl(BlogArticleMapper blogArticleMapper) {
        this.blogArticleMapper = blogArticleMapper;
    }


    /**
     * 查询博客文章
     * 
     * @param id 博客文章主键
     * @return 博客文章
     */
    @Override
    public BlogArticleDO selectBlogArticleById(Integer id)
    {
        return blogArticleMapper.selectBlogArticleById(id);
    }

    /**
     * 查询博客文章列表
     * 
     * @param blogArticle 博客文章
     * @return 博客文章
     */
    @Override
    public PageInfo<BlogArticleDO> selectBlogArticleList(BlogArticleDO blogArticle,int pageNum,int pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<BlogArticleDO> BlogArticleDOList =  blogArticleMapper.selectBlogArticleList(blogArticle);
        PageInfo<BlogArticleDO> BlogArticleDOPageInfo = new PageInfo<>(BlogArticleDOList);
        return PageInfoUtil.pageInfo2DTO(BlogArticleDOPageInfo, BlogArticleDO.class);


    }

    /**
     * 新增博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    public int insertBlogArticle(BlogArticleDO blogArticle)
    {
        blogArticle.setCreateTime(LocalDateTime.now());
        return blogArticleMapper.insertBlogArticle(blogArticle);
    }

    /**
     * 修改博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    public int updateBlogArticle(BlogArticleDO blogArticle)
    {
        blogArticle.setUpdateTime(LocalDateTime.now());
        return blogArticleMapper.updateBlogArticle(blogArticle);
    }

    /**
     * 批量删除博客文章
     * 
     * @param ids 需要删除的博客文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleByIds(Integer[] ids)
    {
        return blogArticleMapper.deleteBlogArticleByIds(ids);
    }

    /**
     * 删除博客文章信息
     * 
     * @param id 博客文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleById(Integer id)
    {
        return blogArticleMapper.deleteBlogArticleById(id);
    }
}
