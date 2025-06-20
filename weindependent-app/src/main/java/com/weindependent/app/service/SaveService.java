package com.weindependent.app.service;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;
/**
 * @author Elly
 * 2025-04-17
 */

public interface SaveService {
    /*收藏文章 */
    int saveBlog(int userId, Integer listId, int blogId);
    /*取消文章收藏 */
    int unsaveBlog(int userId, Integer listId, int blogId);
    
    /*收藏活动 */
    int saveEvent(int userId, int eventId);

    /*  取消活动收藏 */
    int unsaveEvent(int userId, int eventId);

} 
 