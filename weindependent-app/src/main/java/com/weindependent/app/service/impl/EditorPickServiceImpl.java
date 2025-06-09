package com.weindependent.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weindependent.app.convertor.EditorPickConvertor;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.EditorPickDO;
import com.weindependent.app.database.mapper.weindependent.BlogArticleMapper;
import com.weindependent.app.database.mapper.weindependent.EditorPickMapper;
import com.weindependent.app.service.BlogHeroService;
import com.weindependent.app.service.EditorPickService;
import com.weindependent.app.vo.EditorPickVO;

@Service
public class EditorPickServiceImpl implements EditorPickService {
    
    @Resource
    private EditorPickMapper editorPickMapper;

    @Autowired
    private BlogHeroService blogHeroService;

    @Resource
    private BlogArticleMapper blogArticleMapper;

    @Override
    public List<EditorPickVO> getEditorPickArticles(Integer limit) {
        List<EditorPickDO> picks = editorPickMapper.findEditorPickList(limit); 

        if (picks == null || picks.isEmpty()) {
            return new ArrayList<>();
        }

        // 提取所有 articleId，去文章表查详情
        List<Integer> articleIds = picks.stream()
            .map(EditorPickDO::getArticleId)
            .collect(Collectors.toList());

        List<BlogArticleDO> articles = blogArticleMapper.findByIds(articleIds);
        Map<Integer, BlogArticleDO> articleMap = articles.stream()
            .collect(Collectors.toMap(BlogArticleDO::getId, a -> a));

        // 组合成 VO 列表
        List<EditorPickVO> result = new ArrayList<>();
        for (EditorPickDO pick : picks) {
            BlogArticleDO article = articleMap.get(pick.getArticleId());
            if (article != null) {
                // 调用已有的 Convertor
                EditorPickVO vo = EditorPickConvertor.toVO(pick, article);
                // 明确设置 heroType
                vo.setHeroType("Editor's Pick");
                result.add(vo);
            }
        }

        return result;
        
    }

    @Override
    @Transactional
    public boolean addEditorPickArticle(Integer articleId, Integer userId) {
        if (editorPickMapper.isEditorPick(articleId)){
            return true;
        }
        BlogArticleDO article = blogArticleMapper.findById(articleId);

        // 判断文章是否存在
        if (article == null) {
            throw new RuntimeException("文章不存在，无法添加");
        }
    
        // 判断是否为已删除文章
        if (Boolean.TRUE.equals(article.getIsDeleted())) {
            throw new RuntimeException("该文章已被删除，无法添加");
        }
    
        // 判断是否为已发布状态
        if (!"published".equals(article.getArticleStatus())) {
            throw new RuntimeException("该文章未发布，不能添加为编辑精选");
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
        // return editorPickMapper.isEditorPick(articleId);
        return blogHeroService.isEditorPick(articleId);
    }

    //Hurely 新增
    @Override
    public Map<Integer, Boolean> getEditorsPickMapByArticleIds(List<Integer> articleIds) {
        Map<Integer, Boolean> result = new HashMap<>();
        // 遍历每个 Long 类型的 articleId，将其转换为 Integer 调用 isEditorPickArticle 方法
        for (Integer articleId : articleIds) {
            result.put(articleId, isEditorPickArticle(articleId.intValue()));
        }
        return result;
    }

}
