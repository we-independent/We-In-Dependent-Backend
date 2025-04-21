package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.CategoryDO;
import com.weindependent.app.dto.CategoryQry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.weindependent.app.service.ICategoryService;


/**
 * 分类Controller
 * 
 * @author christina
 *    2025-03-23
 */
@Tag(name = "分类管理")
@RestController
@RequestMapping("api/dashboard/category")
public class DashboardCategoryController {
    private final ICategoryService categoryService;

    public DashboardCategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 查询分类列表
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "查询分类列表")
    @PostMapping("/list")
    public PageInfo<CategoryDO> list(@RequestBody CategoryQry categoryQry) {
        return categoryService.selectCategoryListPage(categoryQry);
    }

    /**
     * 获取分类详细信息
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "查询分类详细信息")
    @GetMapping("/{id}")
    public CategoryDO getInfo(@PathVariable("id") Integer id) {
        return categoryService.selectCategoryById(id);
    }

    /**
     * 新增分类
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "新增分类")
    @PostMapping
    public boolean add(@RequestBody CategoryDO category) {
        int userId = StpUtil.getLoginIdAsInt();
        category.setCreateUserId(userId);
        category.setUpdateUserId(userId);
        return categoryService.insertCategory(category) > 0;
    }

    /**
     * 修改分类
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "修改分类")
    @PutMapping
    public boolean edit(@RequestBody CategoryDO category) {
        int userId = StpUtil.getLoginIdAsInt();
        category.setUpdateUserId(userId);
        return categoryService.updateCategory(category) > 0;
    }

    /**
     * 删除分类
     */
    @SignatureAuth
    @SaCheckRole("test")
    @Operation(summary = "删除分类")
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids) {
        int updateUserId = StpUtil.getLoginIdAsInt();
        return categoryService.deleteCategoryByIds(ids, updateUserId) > 0;
    }
}
