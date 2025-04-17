package com.weindependent.app.service;

import java.util.List;
import java.util.Map;

import com.weindependent.app.vo.EditorPickVO;

public interface EditorPickService {
    // 获取编辑精选文章列表
    List<EditorPickVO> getEditorPickArticles(Integer limit);
    
    //将文章标记为编辑精选
    boolean addEditorPickArticle(Integer articleId, Integer userId);

    //取消文章的编辑精选标记
    boolean removeEditorPickArticle(Integer articleId);

    //检查文章是否为编辑精选
    boolean isEditorPickArticle(Integer articleId);

    // Hurely 新增：批量查询多个文章是否为编辑精选，返回 Map<articleId, Boolean>
    Map<Integer, Boolean> getEditorsPickMapByArticleIds(List<Integer> articleIds);
}
