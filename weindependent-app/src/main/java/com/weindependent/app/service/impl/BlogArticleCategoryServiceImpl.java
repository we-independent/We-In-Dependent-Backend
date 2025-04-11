package com.weindependent.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.BlogCategoryDO;
import com.weindependent.app.database.mapper.weindependent.BlogArticleCategoryMapper;
import com.weindependent.app.service.IBlogArticleCategoryService;


/**
 * 获取文章Category
 * 
 * @author Hurely
 *    2025-04-5
 */
@Service
public class BlogArticleCategoryServiceImpl implements IBlogArticleCategoryService {

    @Autowired
    private BlogArticleCategoryMapper blogCategoryMapper;

    @Override
    public List<BlogCategoryDO> selectAllCategories() {
        return blogCategoryMapper.selectAllCategories();
    }
}
