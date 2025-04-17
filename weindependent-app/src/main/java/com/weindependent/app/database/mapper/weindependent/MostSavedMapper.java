package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.EditorPickDO;
import com.weindependent.app.vo.BlogHomePageHeroVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface MostSavedMapper {


    List<EditorPickDO> findCurrentMostSavedList();

    List<EditorPickDO> findPreviousMostSavedList();

    List<BlogHomePageHeroVO> findBlogHomePageHeroVOByIds(@Param("ids") List<Integer> ids);
}
