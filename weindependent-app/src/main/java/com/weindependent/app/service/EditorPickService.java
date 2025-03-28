package com.weindependent.app.service;

import java.util.List;

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
}
