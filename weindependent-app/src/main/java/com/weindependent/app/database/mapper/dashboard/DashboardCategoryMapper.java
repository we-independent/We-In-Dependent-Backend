package com.weindependent.app.database.mapper.dashboard;

import java.util.List;

import com.weindependent.app.database.dataobject.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类Mapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Mapper
public interface DashboardCategoryMapper {
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
     * @param category 分类
     * @return 分类集合
     */
    List<CategoryDO> selectCategoryList(CategoryDO category);

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
     * 删除分类, 软删除
     *
     * @param id 分类主键
     * @return 结果
     */
    int deleteCategoryById(Integer id);

    /**
     * 批量删除分类, 软删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCategoryByIds(Integer[] ids, int updateUserId);

    CategoryDO selectCategoryByName(String name);

    int recoverCategory(CategoryDO existCategory);
}
