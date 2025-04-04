package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.CategoryDO;
import com.weindependent.app.database.mapper.dashboard.TagMapper;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.dto.TagQry;
import com.weindependent.app.utils.PageInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.ITagService;

/**
 * 标签Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author chistina
 *   2025-03-23
 */
@Service
public class TagServiceImpl implements ITagService 
{
    @Autowired
    private TagMapper tagMapper;

    /**
     * 查询标签
     * 
     * @param id 标签主键
     * @return 标签
     */
    @Override
    public TagDO selectTagById(Integer id)
    {
        return tagMapper.selectTagById(id);
    }

    /**
     * 查询标签列表
     * 
     * @param tagQry 标签搜索模板
     * @return 标签
     */
    @Override
    public PageInfo<TagDO> selectTagList(TagQry tagQry)
    {
        TagDO tagDO = new TagDO();
        BeanUtils.copyProperties(tagQry, tagDO);
        PageHelper.startPage(tagQry.getPageNum(), tagQry.getPageSize());
        List<TagDO> TagDOList = tagMapper.selectTagList(tagDO);
        PageInfo<TagDO> pageInfo = new PageInfo<>(TagDOList);
        return PageInfoUtil.pageInfo2DTO(pageInfo, TagDO.class);
    }

    /**
     * 新增标签,
     * 由于是软删除，首先根据新标签名称搜索数据库，
     * 1. 如果已有标签，
     *  1.1 如果已有标签，未被删除 return
     *  1.2 如果已有标签，被删除，恢复标签 return
     * 2. 没有搜索到标签，则新增标签
     * 
     * @param newTag 标签
     * @return 结果
     */
    @Override
    public int insertTag(TagDO newTag)
    {
        TagDO existTag=tagMapper.selectTagByName(newTag.getName());
        if(existTag!=null){
            if(existTag.getIsDeleted()){
                tagMapper.recoverTag(existTag);
                return 0;
            }else{
                return 0;
            }
        }
        newTag.setCreateTime(LocalDateTime.now());
        return tagMapper.insertTag(newTag);
    }

    /**
     * 修改标签
     * 
     * @param tag 标签
     * @return 结果
     */
    @Override
    public int updateTag(TagDO tag)
    {
        tag.setUpdateTime(LocalDateTime.now());
        return tagMapper.updateTag(tag);
    }

    /**
     * 批量删除标签
     * 
     * @param ids 需要删除的标签主键
     * @return 结果
     */
    @Override
    public int deleteTagByIds(Integer[] ids)
    {
        return tagMapper.deleteTagByIds(ids);
    }

    /**
     * 删除标签信息
     * 
     * @param id 标签主键
     * @return 结果
     */
    @Override
    public int deleteTagById(Integer id)
    {
        return tagMapper.deleteTagById(id);
    }
}
