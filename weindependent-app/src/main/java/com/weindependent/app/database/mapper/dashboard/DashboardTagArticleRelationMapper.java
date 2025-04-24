package com.weindependent.app.database.mapper.dashboard;


import com.weindependent.app.database.dataobject.TagArticleRelationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardTagArticleRelationMapper {

    TagArticleRelationDO getRelationByTagIdAndArticleId(int tagId, int articleId);

    int insertTagArticleRelation(TagArticleRelationDO relation);

    void deleteByArticleId(int articleId, int userId);

    int recoverById(int id, int userId);

    List<Integer> getTagIdListByArticleId(int articleId);

}
