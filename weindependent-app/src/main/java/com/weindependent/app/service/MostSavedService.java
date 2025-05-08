package com.weindependent.app.service;


import com.weindependent.app.vo.BlogHomePageHeroVO;
import com.weindependent.app.vo.EditorPickVO;
import com.weindependent.app.dto.BlogArticleCardQry;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MostSavedService {

    List<BlogHomePageHeroVO>  getCurrentBlogHomePageHeroVO();

    List<EditorPickVO> getCurrentMostSavedEditorPickVO();

    List<EditorPickVO> getPreviousMostSavedEditorPickVO();

    List<BlogArticleCardQry> getTopSavedBlogsForColdstart(int topN);

    List<BlogArticleCardQry> getMostSavedArticlesExcludeList(@Param("excludeArticleId") List<Integer> excludeIds, @Param("needed") int needed);

    int setMostSavedEveryTwoWeeks();


}
