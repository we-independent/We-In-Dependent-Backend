package com.weindependent.app.service.impl;

import com.weindependent.app.database.mapper.weindependent.BlogHeroMapper;
import com.weindependent.app.service.BlogHeroService;
import com.weindependent.app.vo.websiteoperation.BlogHeroVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BlogHeroServiceImpl implements BlogHeroService {

    @Autowired
    private BlogHeroMapper blogHeroMapper;

    @Override
    public List<BlogHeroVO> getCurrentBlogHeroVOList() {
        return blogHeroMapper.selectCurrentBlogHeroVOList();
    }
    
    @Override
    public boolean isEditorPick(Integer articleId) {
        return blogHeroMapper.isEditorPickFromHero(articleId);
    }

    @Override
    public List<Integer> getEditorPickArticleIds(int limit) {
        return blogHeroMapper.getEditorPickArticleIdsFromHero(limit);
    }

    @Override
    public Map<Integer, Boolean> getEditorsPickMapByArticleIds(List<Integer> articleIds) {
        List<Integer> editorPickIds = blogHeroMapper.getEditorPickArticleIdsFromHero(null); // null = 不限
        Set<Integer> pickSet = new HashSet<>(editorPickIds);
        log.info("✅ 当前页面文章ID: {}", articleIds);
        log.info("✅ 所有 editorPick ID: {}", pickSet);
        
        return articleIds.stream().collect(Collectors.toMap(
            id -> id,
            id -> pickSet.contains(id)
        ));
    }
}
