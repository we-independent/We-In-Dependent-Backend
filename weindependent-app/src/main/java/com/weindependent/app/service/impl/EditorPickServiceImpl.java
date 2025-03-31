package com.weindependent.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weindependent.app.convertor.EditorPickConvertor;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.EditorPickDO;
import com.weindependent.app.database.mapper.weindependent.BlogArticleMapper;
import com.weindependent.app.database.mapper.weindependent.EditorPickMapper;
import com.weindependent.app.service.EditorPickService;
import com.weindependent.app.vo.EditorPickVO;

@Service
public class EditorPickServiceImpl implements EditorPickService {
    
    @Resource
    private EditorPickMapper editorPickMapper;

    @Resource
    private BlogArticleMapper blogArticleMapper;

    @Override
    public List<EditorPickVO> getEditorPickArticles(Integer limit) {
        List<Integer> articleIds = editorPickMapper.findEditorPickArticleIds(limit);

        if (articleIds == null || articleIds.isEmpty()){
            return new ArrayList<>();
        }
        List<BlogArticleDO> articles = blogArticleMapper.findByIds(articleIds);
        return EditorPickConvertor.toVOList(articles);
        
    }

    @Override
    @Transactional
    public boolean addEditorPickArticle(Integer articleId, Integer userId) {
        if (editorPickMapper.isEditorPick(articleId)){
            return true;
        }
        BlogArticleDO article = blogArticleMapper.findById(articleId);
        if (article == null || !"published".equals(article.getArticleStatus()) || article.getIsDeleted() == 1){
            return false;
        }

        EditorPickDO editorPick = new EditorPickDO();
        editorPick.setArticleId(articleId);
        editorPick.setCreateUserId(userId);
        editorPick.setStatus(1);
        return editorPickMapper.insert(editorPick) > 0;
    }

    @Override
    public boolean removeEditorPickArticle(Integer articleId) {
        // 逻辑删除，将状态设置为无效
        return editorPickMapper.updateStatusByArticleId(articleId, 0) > 0;
    }   

    @Override
    public boolean isEditorPickArticle(Integer articleId) {
        return editorPickMapper.isEditorPick(articleId);
    }
}
