package com.weindependent.app.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.weindependent.BlogArticleListMapper;
import com.weindependent.app.database.dataobject.BlogArticleListDO;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.dto.BlogArticleSinglePageQry;
import com.weindependent.app.dto.BlogCommentQry;
import com.weindependent.app.enums.CategoryEnum;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.IBlogArticleListService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlogArticleListServiceImpl implements IBlogArticleListService {
    @Autowired
    private final BlogArticleListMapper blogArticleListMapper;
    // @Autowired
    // private EditorPickService editorsPickService;
    // @Autowired
    // private SavedCountService savedCountService;

    

    public BlogArticleListServiceImpl(BlogArticleListMapper blogArticleListMapper) {
        this.blogArticleListMapper = blogArticleListMapper;
    }

    @Override
    public PageInfo<BlogArticleListDO> selectBlogArticleList(BlogArticleListQry query) {
        log.info("接收到的 categoryId: " + query.getCategoryId());

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
        List<BlogArticleListDO> list = blogArticleListMapper.selectBlogArticleList(query);
        log.info("查到的文章数量：" + list.size());

        return new PageInfo<>(list);
    }


    // Related Article card
    @Override
    public List<BlogArticleCardQry> getArticlesByCategoryOrTagsExcludeSelf(Integer categoryId, List<Integer> tagIdList,
            int articleId){
            if (categoryId == null || articleId <= 0) {
                log.warn("Invalid parameters for related article fetch: categoryId={}, articleId={}", categoryId, articleId);
                throw new ResponseException(ErrorCode.RELATED_ARTICLE_FETCH_FAILED.getCode(), "没有找到相关文章");
            }
        
            if (tagIdList == null) {
                tagIdList = Collections.emptyList();
            }
        
            log.info("Running related article query: categoryId={}, articleId={}, tagIdList={}",
                    categoryId, articleId, tagIdList);
            List<BlogArticleCardQry> list = blogArticleListMapper.getArticlesByCategoryOrTagsExcludeSelf(categoryId, tagIdList, articleId);
            //跳转single page
            list.forEach(item -> {
                BlogArticleListDO article = blogArticleListMapper.selectBlogArticleById(item.getId());
                String content = article != null ? article.getContent() : null;
                int wordCount = 0;
                if (content != null && !content.isEmpty()) {
                    wordCount = content.trim().split("\\s+").length;
                }
                int readingMinutes = Math.min(30, Math.max(1, (int) Math.ceil(wordCount / 200.0)));
                item.setReadingTime(readingMinutes + " min read");
                String categoryName = CategoryEnum.getNameByCode(String.valueOf(article.getCategoryId()));
                item.setCategoryName(categoryName);
                item.setArticleUrl("/article/" + item.getId());
            });
            log.info("相关文章 candidates 查询结果：{}", list);
            return list;
        }




    // Single Blog Page
    @Override
    public BlogArticleSinglePageQry getArticleDetailById(Integer id, Integer pageNum, Integer pageSize){

        BlogArticleListDO article = blogArticleListMapper.selectBlogArticleById(id);
        if (article == null) {
            log.warn("未找到文章，ID: {}", id);
            throw new RuntimeException("文章不存在");
        }
        BlogArticleSinglePageQry qry = new BlogArticleSinglePageQry();
        qry.setId(article.getId());
        qry.setTitle(article.getTitle());
        qry.setSummary(article.getSummary());
        qry.setContent(article.getContent());
        qry.setAuthorId(article.getAuthorId());
        qry.setAuthorName("We Independent");
        String content = article.getContent();
        int wordCount = (content != null && !content.trim().isEmpty())
            ? content.trim().split("\\s+").length
            : 0;
        int readingMinutes = Math.min(30, Math.max(1, (int)Math.ceil(wordCount / 200.0)));
        qry.setReadingtime(readingMinutes + " min read");
        qry.setBannerImgId(article.getBannerImgId());
        qry.setBannerImageUrl(blogArticleListMapper.selectBannerImageUrlById(article.getBannerImgId()));
        qry.setCategoryId(article.getCategoryId());
        qry.setArticleStatus(article.getArticleStatus());
        qry.setArticleSourceType(article.getArticleSourceType());
        qry.setSourceUrl(article.getSourceUrl());
        qry.setCreateUserId(article.getCreateUserId());
        qry.setCreateTime(article.getCreateTime());
        qry.setUpdateUserId(article.getUpdateUserId());
        qry.setUpdateTime(article.getUpdateTime());
        List<String> tags = blogArticleListMapper.selectTagsByArticleId(id);
        qry.setTags(tags);
        PageHelper.startPage(pageNum, pageSize);
        List<BlogCommentDO> commentDOs = blogArticleListMapper.selectCommentsByArticleId(id);
        List<BlogCommentQry> commentVOs = commentDOs.stream().map(c -> {
            BlogCommentQry commentVO = new BlogCommentQry();
            commentVO.setId(c.getId());
            commentVO.setCreateUserId(c.getCreateUserId());
            commentVO.setParentCommentId(c.getParentCommentId());
            commentVO.setCommentAuthorUserId(c.getCommentAuthorUserId());
            commentVO.setContent(c.getContent());
            commentVO.setCreateTime(c.getCreateTime());
            return commentVO;
            }).collect(Collectors.toList());
        qry.setComments(commentVOs);
        qry.setDisclaimer("The information in this article is for general purposes only. We make no warranties about the accuracy or completeness of the content. Views expressed are those of the author(s) and do not reflect the views of We Independent. We are not responsible for any actions taken based on this information. Please seek professional advice if needed.");

        return qry;
    }
}
