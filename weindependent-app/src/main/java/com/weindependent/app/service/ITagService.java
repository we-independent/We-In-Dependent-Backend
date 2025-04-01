package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.dto.TagQry;

/**
 * 标签Service接口
 * 
 * @author chistina
 *    2025-03-23
 */
public interface ITagService 
{
    /**
     * 查询标签
     * 
     * @param id 标签主键
     * @return 标签
     */
    public TagDO selectTagById(Integer id);

    /**
     * 查询标签列表
     * 
     * @param tagQry 标签查询模板
     * @return 标签集合
     */
    public PageInfo<TagDO> selectTagList(TagQry tagQry);

    /**
     * 新增标签
     * 
     * @param tag 标签
     * @return 结果
     */
    public int insertTag(TagDO tag);

    /**
     * 修改标签
     * 
     * @param tag 标签
     * @return 结果
     */
    public int updateTag(TagDO tag);

    /**
     * 批量删除标签
     * 
     * @param ids 需要删除的标签主键集合
     * @return 结果
     */
    public int deleteTagByIds(Integer[] ids);

    /**
     * 删除标签信息
     * 
     * @param id 标签主键
     * @return 结果
     */
    public int deleteTagById(Integer id);
}
