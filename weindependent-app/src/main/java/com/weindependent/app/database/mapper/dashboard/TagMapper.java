package com.weindependent.app.database.mapper.dashboard;

import java.util.List;

import com.weindependent.app.database.dataobject.TagDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签Mapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author chistina
 * @date 2025-03-23
 */
@Mapper
public interface TagMapper 
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
     * @param tag 标签
     * @return 标签集合
     */
    public List<TagDO> selectTagList(TagDO tag);

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
     * 删除标签, 软删除
     * 
     * @param id 标签主键
     * @return 结果
     */
    public int deleteTagById(Integer id);

    /**
     * 批量删除标签, 软删除
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTagByIds(Integer[] ids);

    TagDO selectTagByName(String name);

    int recoverTag(TagDO existTag);
}
