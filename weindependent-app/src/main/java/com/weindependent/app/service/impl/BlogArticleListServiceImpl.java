package com.weindependent.app.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.weindependent.BlogArticleListMapper;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.service.EditorPickService;
import com.weindependent.app.service.IBlogArticleListService;
import com.weindependent.app.service.SavedCountService;
import com.weindependent.app.vo.BlogArticleVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlogArticleListServiceImpl implements IBlogArticleListService {
    @Autowired
    private final BlogArticleListMapper blogArticleMapper;
    @Autowired
    private EditorPickService editorsPickService;
    @Autowired
    private SavedCountService savedCountService;
    

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
            query.setOrderClause("(SELECT COUNT(1) FROM save_list_article sa WHERE sa.article_id = blog_article.id) DESC, update_time DESC");
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

    @Override
    public BlogArticleCardQry getArticleDetailById(Integer id){

        BlogArticleListDO article = blogArticleMapper.selectBlogArticleById(id);
        if (article == null) {
            log.warn("未找到文章，ID: {}", id);
            throw new RuntimeException("文章不存在");
        }
        BlogArticleCardQry qry = new BlogArticleCardQry();
        qry.setId(article.getId());
        qry.setTitle(article.getTitle());
        // update_time 作为 eventime 返回前端
        qry.setTime(article.getUpdateTime());
        
        // 计算阅读时长：假设每分钟 200 字（常见阅读速度）
        int wordCount = article.getContent() != null ? article.getContent().length() : 0;
        qry.setReadingTime((int)Math.ceil(wordCount / 200.0) + " min");

        // 图片 URL 和链接可根据实际情况替换，目前为固定值
        qry.setImageUrl("BlogArticleImage1");
        qry.setUrl("/articles/" + article.getId()); // 推荐设置跳转路径
        qry.setCategory(String.valueOf(article.getCategoryId()));
        
        // category 直接使用 db 中存储的值，这里转换为字符串；前端可以进一步处理
        List<Integer> articleIds = Arrays.asList(article.getId());
        Map<Integer, Boolean> editorsPickMap = editorsPickService.getEditorsPickMapByArticleIds(articleIds);
        Map<Integer, Integer> savedCountMap = savedCountService.getSavedCountMapByArticleIds(articleIds);
        qry.setEditorsPick(editorsPickMap.getOrDefault(article.getId(), false));
        qry.setSavedCount(savedCountMap.getOrDefault(article.getId(), 0));
        return qry;

    }
}
