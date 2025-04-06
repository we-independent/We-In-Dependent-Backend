package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.dto.BlogArticleQry;


/**
 * 博客文章Service接口
 * 
 * @author christina
 *    2025-03-23
 */
public interface IBlogArticleListService 
{

    /**
     * 查询博客文章列表
     * 
     * @param blogArticleQry 博客文章查询模板
     * @return 博客文章集合
     */
    public PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry blogArticleQry);

}
