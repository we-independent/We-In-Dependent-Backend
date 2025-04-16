package com.weindependent.app.service.impl;

import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.weindependent.BlogArticleListMapper;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.service.IBlogArticleListService;
import org.springframework.stereotype.Service;

@Service
public class BlogArticleListServiceImpl implements IBlogArticleListService {

    private final BlogArticleListMapper blogArticleMapper;

    public BlogArticleListServiceImpl(BlogArticleListMapper blogArticleMapper) {
        this.blogArticleMapper = blogArticleMapper;
    }

    @Override
    public PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry query) {
        System.out.println("接收到的 categoryId: " + query.getCategoryId());

        // 分页容错处理
        int pageNum = (query.getPageNum() != null && query.getPageNum() > 0) ? query.getPageNum() : 1;
        int pageSize = (query.getPageSize() != null && query.getPageSize() > 0) ? query.getPageSize() : 9;

        // 构造 SQL 排序子句
        String orderBy = query.getOrderBy();
        if (orderBy == null || orderBy.trim().isEmpty() || "latest".equalsIgnoreCase(orderBy)) {
            query.setOrderClause("update_time DESC");
        } else if ("most_saved".equalsIgnoreCase(orderBy)) {
            query.setOrderClause("(SELECT COUNT(1) FROM saved_article sa WHERE sa.article_id = blog_article.id) DESC, update_time DESC");
        } else {
            // 默认回退
            query.setOrderClause("update_time DESC");
        }

        System.out.println("最终排序子句: " + query.getOrderClause());

        // 启用分页，不传排序到 PageHelper（排序由 XML 使用 ${orderClause} 动态拼接）
        PageHelper.startPage(pageNum, pageSize);

        // 查询
        List<BlogArticleListDO> list = blogArticleMapper.selectBlogArticleList(query);
        System.out.println("查到的文章数量：" + list.size());

        return new PageInfo<>(list);
    }
}
