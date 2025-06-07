package com.weindependent.app.service.impl;

import com.weindependent.app.database.mapper.weindependent.BlogHeroMapper;
import com.weindependent.app.service.BlogHeroService;
import com.weindependent.app.vo.websiteoperation.BlogHeroVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BlogHeroServiceImpl implements BlogHeroService {

    @Autowired
    private BlogHeroMapper blogHeroMapper;

    @Override
    public List<BlogHeroVO> getCurrentBlogHeroVOList() {
        return blogHeroMapper.selectCurrentBlogHeroVOList();
    }
}
