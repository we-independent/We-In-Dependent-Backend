package com.weindependent.app.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.CategoryDO;
import com.weindependent.app.database.mapper.weindependent.BlogArticleListMapper;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.utils.PageInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.IBlogArticleService;


/**
 * 博客文章Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *  2025-03-23
 */
@Service
public class BlogArticleListServiceImpl implements IBlogArticleListService 
{
    private final BlogArticleListMapper blogArticleMapper;

    public BlogArticleListServiceImpl(BlogArticleListMapper blogArticleMapper) {
        this.blogArticleMapper = blogArticleMapper;
    }

    /**
     * 查询博客文章列表
     * 
     * @param blogArticleQry 博客文章
     * @return 博客文章
     */
    @Override
    public PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry blogArticleQry)
    {
        BlogArticleDO blogArticleDO = new BlogArticleDO();
        // BeanUtils.copyProperties(source, target)，Copy properties from blogArticleQry to blogArticleDO @Hurely
        BeanUtils.copyProperties(blogArticleQry,blogArticleDO);

        // 分页容错
        int pageNum = blogArticleQry.getPageNum() != null && blogArticleQry.getPageNum() > 0 ? blogArticleQry.getPageNum() : 1;
        int pageSize = blogArticleQry.getPageSize() != null && blogArticleQry.getPageSize() > 0 ? blogArticleQry.getPageSize() : 10;
        
        PageHelper.startPage(pageNum, pageSize);
        List<BlogArticleDO> BlogArticleDOList = blogArticleMapper.selectBlogArticleList(blogArticleDO);
        PageInfo<BlogArticleDO> PageInfo = new PageInfo<>(BlogArticleDOList);
        return PageInfoUtil.pageInfo2DTO(PageInfo, BlogArticleListDO.class);
    }
}
