package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.SavedCountDO;
import org.apache.ibatis.annotations.Param;
import com.weindependent.app.database.dataobject.EditorPickDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface MostSavedMapper {
    /**
     * 根据文章 ID 列表批量查询对应的收藏数信息
     * @param articleIds 文章ID列表
     * @return 收藏信息列表
     */
    List<SavedCountDO> getSavedCountsByArticleIds(@Param("articleIds") List<Integer> articleIds);

    List<EditorPickDO> findCurrentMostSavedList();

    List<EditorPickDO> findPreviousMostSavedList();
}
