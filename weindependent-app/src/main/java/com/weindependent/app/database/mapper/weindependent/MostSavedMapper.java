package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.EditorPickDO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface MostSavedMapper {


    List<EditorPickDO> findCurrentMostSavedList();

    List<EditorPickDO> findPreviousMostSavedList();
}
