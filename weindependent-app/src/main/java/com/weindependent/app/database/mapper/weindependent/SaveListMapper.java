package com.weindependent.app.database.mapper.weindependent;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;

public interface SaveListMapper {
    List<BlogArticleDO> getSavedBlogsFromList(@Param("listId") int listId);

    SaveListDO findDefaultListByUserId(@Param("userId") int userId);

    int createList(SaveListDO saveListDO);

    /*TODO: 删除之后 所属文章处理未知 */
    int deleteList(@Param("userId") int userId, @Param("listId") int listId);

    List<SaveListDO> getSavedList(@Param("userId") int userId);

    /*检查user是否拥有某收藏夹 */
    int checkListOwnership(@Param("userId")int userId, @Param("listId")int listId);

    List<SaveListDO> findArticleBelongsList(@Param("userId")int userId, @Param("blogId") int blogId);

}
