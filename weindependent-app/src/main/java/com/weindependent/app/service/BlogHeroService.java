package com.weindependent.app.service;

import com.weindependent.app.vo.websiteoperation.BlogHeroVO;

import java.util.List;
import java.util.Map;


public interface BlogHeroService {
    List<BlogHeroVO> getCurrentBlogHeroVOList();
    boolean isEditorPick(Integer articleId);
    List<Integer> getEditorPickArticleIds(int limit);
    Map<Integer, Boolean> getEditorsPickMapByArticleIds(List<Integer> articleIds);

}
