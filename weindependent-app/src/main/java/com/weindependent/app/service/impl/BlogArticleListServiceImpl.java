
package com.weindependent.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.database.mapper.weindependent.BlogArticleListMapper;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.service.IBlogArticleListService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogArticleListServiceImpl implements IBlogArticleListService {

    private final BlogArticleListMapper blogArticleMapper;

    public BlogArticleListServiceImpl(BlogArticleListMapper blogArticleMapper) {
        this.blogArticleMapper = blogArticleMapper;
    }

    @Override
    public PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry blogArticleQry) {
        BlogArticleListDO blogArticleDO = new BlogArticleListDO();
        BeanUtils.copyProperties(blogArticleQry, blogArticleDO);

        int pageNum = blogArticleQry.getPageNum() != null && blogArticleQry.getPageNum() > 0 ? blogArticleQry.getPageNum() : 1;
        int pageSize = blogArticleQry.getPageSize() != null && blogArticleQry.getPageSize() > 0 ? blogArticleQry.getPageSize() : 10;

        if (blogArticleQry.getOrderBy() != null && !blogArticleQry.getOrderBy().isEmpty()) {
            PageHelper.orderBy(blogArticleQry.getOrderBy());
        }

        PageHelper.startPage(pageNum, pageSize);
        List<BlogArticleListDO> BlogArticleDOList = blogArticleMapper.selectBlogArticleList(blogArticleDO);
        return new PageInfo<>(BlogArticleDOList);
    }

    @Override
    public List<BlogArticleListDO> selectColdStartArticles() {
        PageHelper.startPage(1, 5);
        PageHelper.orderBy("update_time desc");
        BlogArticleListDO emptyQuery = new BlogArticleListDO();
        return blogArticleMapper.selectBlogArticleList(emptyQuery);
    }
}
