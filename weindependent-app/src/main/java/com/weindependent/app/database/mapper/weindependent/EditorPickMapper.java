package com.weindependent.app.database.mapper.weindependent;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.weindependent.app.database.dataobject.EditorPickDO;
import java.util.List;

@Mapper
public interface EditorPickMapper {
    int insert(EditorPickDO editorPick);

    int deleteByArticleId(@Param("articleId") Integer articleId);

    List<Integer> findEditorPickArticleIds(@Param("limit") Integer limit);

    boolean isEditorPick(@Param("articleId") Integer articleId);

    int updateStatusByArticleId(@Param("articleId") Integer articleId, @Param("status") Integer status);

    List<EditorPickDO> findEditorPickList(@Param("limit") Integer limit);
}
