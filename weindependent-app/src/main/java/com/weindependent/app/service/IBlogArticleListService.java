package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;

/**
 * 博客文章 Service 接口
 * 
 * 定义查询博客文章列表的方法
 * 
 * @author Hurely
 *    2025-04-5
 */
public interface IBlogArticleListService {

    /**
     * 查询博客文章列表（带分页和排序）
     * 
     * @param query 博客文章查询参数（包括分页、筛选、排序信息）
     * @return 博客文章列表 PageInfo 对象
     */
    PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry query);
}
