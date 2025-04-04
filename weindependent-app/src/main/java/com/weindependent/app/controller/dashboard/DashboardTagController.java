package com.weindependent.app.controller.dashboard;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.annotation.SignatureAuth;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.dto.TagQry;
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


/**
 * 标签Controller
 * 
 * @author chistina
 *    2025-03-23
 */
@Tag(name = "标签管理")
@RestController
@RequestMapping("api/dashboard/tag")
public class DashboardTagController
{
    @Autowired
    private ITagService tagService;

    /**
     * 查询标签列表
     */
    @SignatureAuth
    @Operation(summary = "查询标签列表")
    @PostMapping("/list")
    public PageInfo<TagDO> list(@RequestBody TagQry tagQry)
    {
        return  tagService.selectTagList(tagQry);
    }

    /**
     * 查询标签详细信息
     */
    @SignatureAuth
    @Operation(summary = "查询标签详细信息")
    @GetMapping(value = "/{id}")
    public TagDO getInfo(@PathVariable("id") Integer id)
    {
        return tagService.selectTagById(id);
    }

    /**
     * 新增标签
     */
    @SignatureAuth
    @Operation(summary = "新增标签")
    @PostMapping
    public boolean add(@RequestBody TagDO tag)
    {
        return tagService.insertTag(tag) > 0;
    }

    /**
     * 修改标签
     */
    @SignatureAuth
    @Operation(summary = "修改标签")
    @PutMapping
    public boolean edit(@RequestBody TagDO tag)
    {
        return tagService.updateTag(tag) > 0;
    }

    /**
     * 删除标签
     */
    @SignatureAuth
    @Operation(summary = "删除标签")
	@DeleteMapping("/{ids}")
    public boolean remove(@PathVariable Integer[] ids)
    {
        return tagService.deleteTagByIds(ids) > 0;
    }
}
