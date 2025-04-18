package com.weindependent.app.service;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;
/**
 * @author Elly
 * 2025-04-17
 */

public interface SaveBlogService {
    /*收藏 */
    int saveBlog(int userId, Integer listId, int blogId);
    /*取消收藏 */
    int unsaveBlog(int userId, Integer listId, int blogId);
    /*获取某收藏夹中所有文章 */
    List<BlogArticleDO> getSavedBlogs(int userId, int listId);
    /*获取所有收藏夹 */
    List<SaveListDO>getSavingList(int userId);
    /*创建收藏夹*/
    int createList(int userId, String listName);

} 
 