package com.weindependent.app.service;

import java.util.List;
import com.weindependent.app.database.dataobject.BlogCategoryDO;


/**
 * 获取文章Category
 * 
 * @author Hurely
 *    2025-04-5
 */
public interface IBlogArticleCategoryService {
    List<BlogCategoryDO> selectAllCategories(); 
}