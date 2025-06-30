package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;

/**
 * @author Elly
 * 2025-04-17
 */

public interface SaveService {
    /*收藏文章 */
    int saveBlog(int userId, Integer listId, int blogId);
    /*取消文章收藏 */
    int unsaveBlog(int userId, Integer listId, int blogId);

    boolean blogIsSaved(int userId, int blogId);

    /*显示所有收藏文章(published + not deleted) */
    PageInfo<BlogArticleCardQry> allSavedArticles(BlogArticleListQry query, int userId);
    
    /*收藏活动 */
    int saveEvent(int userId, int eventId);

    /*  取消活动收藏 */
    int unsaveEvent(int userId, int eventId);

} 
 