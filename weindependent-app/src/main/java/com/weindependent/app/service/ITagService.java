package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.dto.TagQry;
import com.weindependent.app.vo.TagCategoryVO;

import java.util.List;

/**
 * 标签Service接口
 * 
 * @author chistina
 *    2025-03-23
 */
public interface ITagService {
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
     * @param tagQry 标签查询模板
     * @return 标签集合
     */
    PageInfo<TagDO> selectTagList(TagQry tagQry);

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
     * 批量删除标签
     *
     * @param ids 需要删除的标签主键集合
     * @return 结果
     */
    int deleteTagByIds(Integer[] ids, int updateUserId);

    /**
     * 删除标签信息
     *
     * @param id 标签主键
     * @return 结果
     */
    int deleteTagById(Integer id);

    /**
     * 查询所有未删除的标签
     * @return 返回标签的id、标签的name、标签所属分类的id和标签所属分类的name。如果标签没有所属分类或所属分类已经删除，则TagCategoryVO中的categoryId和categoryName 为 null
     */
    List<TagCategoryVO> selectAllTagList();
}
