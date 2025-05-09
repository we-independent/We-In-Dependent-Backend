package com.weindependent.app.service;

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
    
    /*收藏活动 */
    int saveEvent(int userId, int eventId);

    /*  取消活动收藏 */
    int unsaveEvent(int userId, int eventId);

} 
 