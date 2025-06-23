package com.weindependent.app.service;

import java.util.List;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;

public interface SaveListService {
    /*获取某收藏夹中所有文章 */
    List<BlogArticleDO> getSavedBlogs(int userId, int listId);
    /*获取所有收藏夹 */
    List<SaveListDO>getSavingList(int userId);
    /*创建收藏夹*/
    int createList(int userId, String listName);
    /*创建默认收藏夹，返回list id */
    int createDefaultList(int userId);
    /*删除收藏夹 */
    int deleteList(int userId, int listId);
    List<SaveListDO>findArticleBelongsList(int userId, int blogId);
}
