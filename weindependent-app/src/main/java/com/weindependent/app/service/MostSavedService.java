package com.weindependent.app.service;


import com.weindependent.app.dto.BlogArticleCardQry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MostSavedService {

    List<BlogArticleCardQry> getTopSavedBlogsForColdstart(int topN);

    List<BlogArticleCardQry> getMostSavedArticlesExcludeList(@Param("excludeArticleId") List<Integer> excludeIds, @Param("needed") int needed);

}
