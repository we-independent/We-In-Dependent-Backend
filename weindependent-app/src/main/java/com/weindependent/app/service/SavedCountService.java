package com.weindependent.app.service;

import java.util.List;
import java.util.Map;

public interface SavedCountService {
    /**
     * 批量查询指定文章的收藏数
     * @param articleIds 文章ID列表
     * @return key: articleId, value: 收藏数
     */
    Map<Integer, Integer> getSavedCountMapByArticleIds(List<Integer> articleIds);
}