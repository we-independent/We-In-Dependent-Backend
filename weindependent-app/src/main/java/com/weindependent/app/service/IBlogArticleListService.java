
package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;
import java.util.List;

/**
 * 博客文章Service接口
 */
public interface IBlogArticleListService {

    /**
     * 查询博客文章列表
     */
    PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry blogArticleQry);

    /**
     * 冷启动推荐博客文章
     */
    List<BlogArticleListDO> selectColdStartArticles();
}
