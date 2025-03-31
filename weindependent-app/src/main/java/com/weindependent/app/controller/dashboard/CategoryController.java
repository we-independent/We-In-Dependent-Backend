package com.weindependent.app.controller.dashboard;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.CategoryDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.service.ICategoryService;


/**
 * 分类Controller
 * 
 * @author christina
 *    2025-03-23
 */
@Tag(name = "分类管理")
@RestController
@RequestMapping("/dashboard/category")
public class CategoryController
{
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 查询分类列表
     */
    @SignatureAuth
    @Operation(summary = "查询分类列表")
    @GetMapping("/list")
    public PageInfo<CategoryDO> list(@RequestBody Map<String, Object> requestMap)
    {
        int pageNum = (int) requestMap.get("pageNum");
        int pageSize = (int) requestMap.get("pageSize");
        JSONObject jsonObject = JSON.parseObject((String) requestMap.get("data"));
        CategoryDO category =jsonObject.toJavaObject(CategoryDO.class);
        return categoryService.selectCategoryList(category, pageNum, pageSize);
    }



    /**
     * 获取分类详细信息
     */
    @SignatureAuth
    @Operation(summary = "查询分类详细信息")
    public  CategoryDO getInfo(@PathVariable("id") Integer id)
    {
        return categoryService.selectCategoryById(id);
    }

    /**
     * 新增分类
     */
    @SignatureAuth
    @Operation(summary = "新增分类")
    @PostMapping
    public boolean add(@RequestBody CategoryDO category)
    {
        return categoryService.insertCategory(category) > 0;
    }

    /**
     * 修改分类
     */
    @SignatureAuth
    @Operation(summary = "修改分类")
    @PutMapping
    public boolean edit(@RequestBody CategoryDO category)
    {
        return categoryService.updateCategory(category) > 0;
    }

    /**
     * 删除分类
     */
    @SignatureAuth
    @Operation(summary = "删除分类")
	@DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids)
    {
        return categoryService.deleteCategoryByIds(ids) > 0;
    }
}
