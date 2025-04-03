package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.database.mapper.dashboard.CategoryMapper;
import com.weindependent.app.database.dataobject.CategoryDO;
import com.weindependent.app.dto.CategoryQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.utils.PageInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.ICategoryService;

/**
 * 分类Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Service
public class CategoryServiceImpl implements ICategoryService 
{
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询分类
     * 
     * @param id 分类主键
     * @return 分类
     */
    @Override
    public CategoryDO selectCategoryById(Integer id)
    {
        return categoryMapper.selectCategoryById(id);
    }

    /**
     * 查询分类列表分页
     * 
     * @param categoryQry 分类查询模板
     * @return 分类
     */
    @Override
    public PageInfo<CategoryDO> selectCategoryListPage(CategoryQry categoryQry)
    {
        CategoryDO categoryDO = new CategoryDO();
        BeanUtils.copyProperties(categoryQry,categoryDO);
        PageHelper.startPage(categoryQry.getPageNum(), categoryQry.getPageSize());
        List<CategoryDO> CategoryDOList =  categoryMapper.selectCategoryList(categoryDO);
        PageInfo<CategoryDO> CategoryDOPageInfo = new PageInfo<>(CategoryDOList);
        return PageInfoUtil.pageInfo2DTO(CategoryDOPageInfo, CategoryDO.class);
    }

    /**
     * 新增分类
     * 由于是软删除，首先根据新分类名称搜索数据库，
     * 1. 如果已有分类，
     *  1.1 如果已有分类，未被删除 return
     *  1.2 如果已有分类，被删除，恢复分类 return
     * 2. 没有搜索到分类，则新增分类
     *
     * @param category 分类
     * @return 结果
     */
    @Override
    public int insertCategory(CategoryDO category) {
        CategoryDO existCategory = categoryMapper.selectCategoryByName(category.getName());
        if (existCategory != null) {
            if (existCategory.getIsDeleted()) {
                categoryMapper.recoverCategory(existCategory);
                throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "被删除分类已经恢复");
            } else {
                throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "同名分类已经存在");
            }
        }
        category.setCreateTime(LocalDateTime.now());
        return categoryMapper.insertCategory(category);
    }

    /**
     * 修改分类
     * 
     * @param category 分类
     * @return 结果
     */
    @Override
    public int updateCategory(CategoryDO category)
    {
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateCategory(category);
    }

    /**
     * 批量删除分类
     * 
     * @param ids 需要删除的分类主键
     * @return 结果
     */
    @Override
    public int deleteCategoryByIds(Integer[] ids)
    {
        return categoryMapper.deleteCategoryByIds(ids);
    }

    /**
     * 删除分类信息
     * 
     * @param id 分类主键
     * @return 结果
     */
    @Override
    public int deleteCategoryById(Integer id)
    {
        return categoryMapper.deleteCategoryById(id);
    }
}
