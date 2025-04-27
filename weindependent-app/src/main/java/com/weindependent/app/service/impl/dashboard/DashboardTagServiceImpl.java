package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.dashboard.DashboardTagMapper;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.dto.TagQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.vo.TagCategoryVO;
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
public class DashboardTagServiceImpl implements ITagService {
    @Autowired
    private DashboardTagMapper dashboardTagMapper;

    /**
     * 查询标签
     *
     * @param id 标签主键
     * @return 标签
     */
    @Override
    public TagDO selectTagById(Integer id) {
        return dashboardTagMapper.selectTagById(id);
    }

    /**
     * 查询标签列表
     *
     * @param tagQry 标签搜索模板
     * @return 标签
     */
    @Override
    public PageInfo<TagDO> selectTagList(TagQry tagQry) {
        TagDO tagDO = new TagDO();
        BeanUtils.copyProperties(tagQry, tagDO);
        PageHelper.startPage(tagQry.getPageNum(), tagQry.getPageSize());
        List<TagDO> TagDOList = dashboardTagMapper.selectTagList(tagDO);
        PageInfo<TagDO> pageInfo = new PageInfo<>(TagDOList);
        return PageInfoUtil.pageInfo2DTO(pageInfo, TagDO.class);
    }

    /**
     * 新增标签,
     * 由于是软删除，首先根据新标签名称搜索数据库，
     * 1. 如果已有标签，
     * 1.1 如果已有标签，未被删除 return
     * 1.2 如果已有标签，被删除，恢复标签 return
     * 2. 没有搜索到标签，则新增标签
     *
     * @param newTag 标签
     * @return 结果
     */
    @Override
    public int insertTag(TagDO newTag) {
        TagDO existTag = dashboardTagMapper.selectTagByName(newTag.getName());
        if (existTag != null) {
            if (existTag.getIsDeleted()) {
                dashboardTagMapper.recoverTag(existTag);
                throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "被删除Tag已经恢复");
            } else {
                throw new ResponseException(ErrorCode.UNDEFINED_ERROR.getCode(), "同名Tag已经存在");
            }
        }
        newTag.setCreateTime(LocalDateTime.now());
        return dashboardTagMapper.insertTag(newTag);
    }

    /**
     * 修改标签
     *
     * @param tag 标签
     * @return 结果
     */
    @Override
    public int updateTag(TagDO tag) {
        tag.setUpdateTime(LocalDateTime.now());
        return dashboardTagMapper.updateTag(tag);
    }

    /**
     * 批量删除标签
     *
     * @param ids 需要删除的标签主键
     * @return 结果
     */
    @Override
    public int deleteTagByIds(Integer[] ids, int updateUserId) {
        return dashboardTagMapper.deleteTagByIds(ids, updateUserId);
    }

    /**
     * 删除标签信息
     *
     * @param id 标签主键
     * @return 结果
     */
    @Override
    public int deleteTagById(Integer id) {
        return dashboardTagMapper.deleteTagById(id);
    }

    /**
     * 查询所有未删除的标签
     * @return 返回标签的id、标签的name、标签所属分类的id和标签所属分类的name。如果标签没有所属分类或所属分类已经删除，则TagCategoryVO中的categoryId和categoryName 为 null
     */
    @Override
    public List<TagCategoryVO> selectAllTagList() {
        return dashboardTagMapper.selectAllTagList();
    }
}
