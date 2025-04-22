package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.CategoryDO;
import com.weindependent.app.dto.CategoryQry;

import java.util.List;

/**
 * 分类Service接口
 * 
 * @author christina
 *    2025-03-23
 */
public interface ICategoryService {
    /**
     * 查询分类
     *
     * @param id 分类主键
     * @return 分类
     */
    CategoryDO selectCategoryById(Integer id);

    /**
     * 查询分类列表
     *
     * @param categoryQry 分类查询模板
     * @return 分类集合
     */
    PageInfo<CategoryDO> selectCategoryListPage(CategoryQry categoryQry);

    /**
     * 查询所有分类列表
     *
     * @return 所有分类列表
     */
    List<CategoryDO> selectAllCategory();

    /**
     * 新增分类
     *
     * @param category 分类
     * @return 结果
     */
    int insertCategory(CategoryDO category);

    /**
     * 修改分类
     *
     * @param category 分类
     * @return 结果
     */
    int updateCategory(CategoryDO category);

    /**
     * 批量删除分类
     *
     * @param ids 需要删除的分类主键集合
     * @return 结果
     */
    int deleteCategoryByIds(Integer[] ids, int updateUserId);

    /**
     * 删除分类信息
     *
     * @param id 分类主键
     * @return 结果
     */
    int deleteCategoryById(Integer id);


}
