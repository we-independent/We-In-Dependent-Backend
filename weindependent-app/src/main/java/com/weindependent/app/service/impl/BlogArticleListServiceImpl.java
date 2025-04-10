package com.weindependent.app.service.impl;

import java.util.Arrays;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.weindependent.BlogArticleListMapper;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogArticleListService;


/**
 * 博客文章List Service Implant
 *
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
     * 并实现分页，page Number default 1，page Size default 10
     * 排序，含白名单校验
     * 
     * @param blogArticleListQry 博客文章
     * @return 博客文章列表
     */
    @Override
    public PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry blogArticleQry)
    {
        System.out.println("接收到的 categoryId: " + blogArticleQry.getCategoryId());

        BlogArticleListDO blogArticleDO = new BlogArticleListDO();
        // BeanUtils.copyProperties(source, target)，Copy properties from blogArticleQry to blogArticleDO @Hurely
        BeanUtils.copyProperties(blogArticleQry,blogArticleDO);

        // 分页容错
        int pageNum = blogArticleQry.getPageNum() != null && blogArticleQry.getPageNum() > 0 ? blogArticleQry.getPageNum() : 1;
        int pageSize = blogArticleQry.getPageSize() != null && blogArticleQry.getPageSize() > 0 ? blogArticleQry.getPageSize() : 10;
        
        // 排序参数校验（必须在分页前做）
        sanitizeOrderBy(blogArticleQry);

        // System.out.println("PageHelper 启动分页参数 pageNum=" + pageNum + ", pageSize=" + pageSize);
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 校验并排序
        if (blogArticleQry.getOrderBy() != null && !blogArticleQry.getOrderBy().isEmpty()) {
            PageHelper.orderBy(blogArticleQry.getOrderBy());
        }

        List<BlogArticleListDO> BlogArticleDOList = blogArticleMapper.selectBlogArticleList(blogArticleDO);

        // ✅ 打印查到的结果数量
        System.out.println("查到的文章数量：" + BlogArticleDOList.size());
        // 直接返回 PageHelper 提供的分页对象
        return new PageInfo<>(BlogArticleDOList);
        // PageInfo<BlogArticleListDO> PageInfo = new PageInfo<>(BlogArticleDOList);

        // return PageInfoUtil.pageInfo2DTO(PageInfo, BlogArticleListDO.class);
    }

    // 白名单

    private static final List<String> ALLOWED_ORDER_FIELDS = Arrays.asList(
        "create_time", "update_time", "title"
    );

    private static final List<String> ALLOWED_DIRECTIONS = Arrays.asList("asc", "desc");

    private void sanitizeOrderBy(BlogArticleListQry query) {
        String orderBy = query.getOrderBy();

        // 1. 没传，默认值
        if (orderBy == null || orderBy.trim().isEmpty()) {
            query.setOrderBy("update_time desc");
            return;
        }

        // 2. 拆解字段和排序方向
        String[] parts = orderBy.trim().toLowerCase().split("\\s+");

        String field = parts[0];
        String direction = parts.length > 1 ? parts[1] : "desc";

        // 3. 校验字段是否合法
        if (!ALLOWED_ORDER_FIELDS.contains(field) || !ALLOWED_DIRECTIONS.contains(direction)) {
            query.setOrderBy("update_time desc");
            return;
        }

        // 4. 设置安全值
        query.setOrderBy(field + " " + direction);
    }
}
