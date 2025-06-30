package com.weindependent.app.service.impl;

import com.weindependent.app.database.mapper.weindependent.MostSavedMapper;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.MostSavedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MostSavedServiceImpl implements MostSavedService {
    @Resource
    private MostSavedMapper mostSavedMapper;

    @Override
    public List<BlogArticleCardQry> getMostSavedArticlesExcludeList(List<Integer> excludeIds, int needed) {
        return mostSavedMapper.getMostSavedArticlesExcludeList(excludeIds, needed);
    }

    @Override
    public List<BlogArticleCardQry> getTopSavedBlogsForColdstart(int topN) {
        List<BlogArticleCardQry> blogCards = mostSavedMapper.findTopSavedBlogs(topN);

        if (blogCards == null || blogCards.isEmpty()) {
            throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "没有查询到Top Saved博客");
        }

        for (BlogArticleCardQry card : blogCards) {
            // 1. 计算阅读时长
            if (card.getContent() != null) {
                int wordCount = card.getContent().trim().split("\\s+").length;
                int readingTimeMinutes = Math.min(30, Math.max(1, (int) Math.ceil(wordCount / 200.0)));
                card.setReadingTime(readingTimeMinutes + " min");
            } else {
                card.setReadingTime("1 min");  // 防止null
            }

            // 2. 设置 articleUrl (href)
            card.setArticleUrl("/blogs/" + card.getCategoryName().toLowerCase() + "/" + card.getId());

            // 3. 去掉 content，不传给前端
            card.setContent(null);

            // 4. author默认都是We Independence
            card.setAuthor("We Independence");
        }

        return blogCards;
    }

}
