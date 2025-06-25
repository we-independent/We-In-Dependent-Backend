package com.weindependent.app.database.mapper.weindependent;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weindependent.app.database.dataobject.SaveListDO;
import com.weindependent.app.dto.BlogArticleCardQry;

public interface SaveListMapper {
    List<BlogArticleCardQry> getSavedBlogsFromList(@Param("userId") int userId, @Param("listId") int listId);

    Integer findDefaultListByUserId(@Param("userId") int userId);

    int createList(SaveListDO saveListDO);

    int deleteList(@Param("userId") int userId, @Param("listId") int listId);

    List<SaveListDO> getSavedList(@Param("userId") int userId);

    /*检查user是否拥有某收藏夹 */
    boolean checkListOwnership(@Param("userId")int userId, @Param("listId")int listId);

    int updateListName(@Param("userId")int userId, @Param("listId")int listId, @Param("name") String name);

    List<SaveListDO> findArticleBelongsList(@Param("userId")int userId, @Param("blogId") int blogId);

    int updateSaveToListMetadata(@Param("listId") int listId, @Param("blogId") int blogId);

    int updateDeleteFromListMetadata(@Param("listId") int listId, @Param("blogId") int blogId);

}
