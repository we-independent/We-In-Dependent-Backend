package com.weindependent.app.database.mapper.dashboard;


import com.weindependent.app.database.dataobject.TagArticleRelationDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DashboardTagArticleRelationMapper {

    TagArticleRelationDO getRelationByTagIdAndArticleId(int tagId, int articleId);

    int insertTagArticleRelation(TagArticleRelationDO relation);

    void deleteByArticleId(int articleId, int userId);

    int recoverById(int id, int userId);

    List<Integer> getTagIdListByArticleId(int articleId);
    
    int deleteByArticleIdExcludeTags(@Param("articleId") Integer articleId,
                                     @Param("tagIdList") List<Integer> tagIdList,
                                     @Param("userId") Integer userId);
    int updateAndInsertTagArticleRelations(@Param("list") List<TagArticleRelationDO> relationList);

}
