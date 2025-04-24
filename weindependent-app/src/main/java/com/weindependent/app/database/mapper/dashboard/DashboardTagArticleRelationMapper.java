package com.weindependent.app.database.mapper.dashboard;


import com.weindependent.app.database.dataobject.TagArticleRelationDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashboardTagArticleRelationMapper {

    TagArticleRelationDO getRelationByTagIdAndArticleId(Integer tagId, int articleId);

    int insertTagArticleRelation(TagArticleRelationDO relation);

    void deleteByArticleId(int articleId, int userId);

    int recoverById(Integer id, int userId);

}
