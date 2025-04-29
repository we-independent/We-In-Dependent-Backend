package com.weindependent.app.database.mapper.dashboard;

import java.util.List;

import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.vo.TagCategoryVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签Mapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author chistina
 *    2025-03-23
 */
@Mapper
public interface DashboardTagMapper {
    /**
     * 查询标签
     *
     * @param id 标签主键
     * @return 标签
     */
    TagDO selectTagById(Integer id);

    /**
     * 查询标签列表
     *
     * @param tag 标签
     * @return 标签集合
     */
    List<TagDO> selectTagList(TagDO tag);

    /**
     * 新增标签
     *
     * @param tag 标签
     * @return 结果
     */
    int insertTag(TagDO tag);

    /**
     * 修改标签
     *
     * @param tag 标签
     * @return 结果
     */
    int updateTag(TagDO tag);

    /**
     * 删除标签, 软删除
     *
     * @param id 标签主键
     * @return 结果
     */
    int deleteTagById(Integer id);

    /**
     * 批量删除标签, 软删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTagByIds(Integer[] ids, int updateUserId);

    TagDO selectTagByName(String name);

    int recoverTag(TagDO existTag);

    /**
     * 查询所有未删除的标签
     * @return 返回标签的id、标签的name、标签所属分类的id和标签所属分类的name。如果标签没有所属分类或所属分类已经删除，则TagCategoryVO中的categoryId和categoryName 为 null
     */
    List<TagCategoryVO> selectAllTagList();
}
