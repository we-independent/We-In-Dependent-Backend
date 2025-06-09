package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.vo.websiteoperation.BlogHeroVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BlogHeroMapper {
    List<BlogHeroVO> selectCurrentBlogHeroVOList();
    boolean isEditorPickFromHero(@Param("articleId") Integer articleId);
    List<Integer> getEditorPickArticleIdsFromHero(@Param("limit") Integer limit);
    List<Long> findArticleIdsByHeroType(@Param("heroType") String heroType);

}
