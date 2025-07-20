package com.weindependent.app.controller.dashboard;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.dto.TagQry;
import com.weindependent.app.vo.TagCategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.service.ITagService;

import java.util.List;


/**
 * 标签Controller
 * 
 * @author chistina
 *    2025-03-23
 */
@Tag(name = "标签管理")
@RestController
@RequestMapping("api/dashboard/tag")
public class DashboardTagController {
    @Autowired
    private ITagService tagService;

    /**
     * 查询标签列表
     */
    @SaCheckRole("admin")
    @Operation(summary = "查询标签列表")
    @PostMapping("/list")
    public PageInfo<TagDO> list(@RequestBody TagQry tagQry) {
        
        return tagService.selectTagList(tagQry);
    }
    /**
     * 查询所有未删除的标签
     * 返回标签的id、标签的name、标签所属分类的id和标签所属分类的name。如果标签没有所属分类或所属分类已经删除，则TagCategoryVO中的categoryId和categoryName 为 null
     */
    @SaCheckRole("admin")
    @Operation(summary = "查询标签列表")
    @GetMapping("/all")
    public List<TagCategoryVO> list() {
        return tagService.selectAllTagList();
    }

    /**
     * 查询标签详细信息
     */
    @SaCheckRole("admin")
    @Operation(summary = "查询标签详细信息")
    @GetMapping(value = "/{id}")
    public TagDO getInfo(@PathVariable("id") Integer id) {
        return tagService.selectTagById(id);
    }

    /**
     * 新增标签
     */
    @SaCheckRole("admin")
    @Operation(summary = "新增标签")
    @PostMapping
    public boolean add(@RequestBody TagDO tag) {
        int userId = StpUtil.getLoginIdAsInt();
        tag.setCreateUserId(userId);
        tag.setUpdateUserId(userId);
        return tagService.insertTag(tag) > 0;
    }

    /**
     * 修改标签
     */
    @SaCheckRole("admin")
    @Operation(summary = "修改标签")
    @PutMapping
    public boolean edit(@RequestBody TagDO tag) {
        int userId = StpUtil.getLoginIdAsInt();
        tag.setUpdateUserId(userId);
        return tagService.updateTag(tag) > 0;
    }

    /**
     * 删除标签
     */
    @SaCheckRole("admin")
    @Operation(summary = "删除标签")
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids) {
        int updateUserId = StpUtil.getLoginIdAsInt();
        return tagService.deleteTagByIds(ids, updateUserId) > 0;
    }
}
