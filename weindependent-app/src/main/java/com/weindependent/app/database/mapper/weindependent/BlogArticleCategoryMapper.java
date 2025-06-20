package com.weindependent.app.database.mapper.weindependent;

import java.util.List;

import com.weindependent.app.database.dataobject.BlogCategoryDO;


/**
 * 获取文章Category的名字
 * 
 * @author Hurely
 *    2025-04-5
 */
public interface BlogArticleCategoryMapper {
    List<BlogCategoryDO> selectAllCategories();
}