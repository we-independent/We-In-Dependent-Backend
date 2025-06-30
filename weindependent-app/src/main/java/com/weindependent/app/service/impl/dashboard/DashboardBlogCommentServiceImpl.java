package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogCommentMapper;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogCommentQry;
import com.weindependent.app.utils.PageInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogCommentService;

/**
 * 博客评论Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Service
public class DashboardBlogCommentServiceImpl implements IBlogCommentService {
    @Autowired
    private DashboardBlogCommentMapper dashboardBlogCommentMapper;

    /**
     * 查询博客评论
     *
     * @param id 博客评论主键
     * @return 博客评论
     */
    @Override
    public BlogCommentDO selectBlogCommentById(Integer id) {
        return dashboardBlogCommentMapper.selectBlogCommentById(id);
    }

    /**
     * 查询博客评论列表
     *
     * @param blogCommentQry 博客评论
     * @return 博客评论
     */
    @Override
    public PageInfo<BlogCommentDO> selectBlogCommentList(BlogCommentQry blogCommentQry) {

        BlogCommentDO blogCommentDO = new BlogCommentDO();
        BeanUtils.copyProperties(blogCommentQry, blogCommentDO);
        PageHelper.startPage(blogCommentQry.getPageNum(), blogCommentQry.getPageSize());
        List<BlogCommentDO> blogCommentDOList = dashboardBlogCommentMapper.selectBlogCommentList(blogCommentDO);
        PageInfo<BlogCommentDO> PageInfo = new PageInfo<>(blogCommentDOList);
        return PageInfoUtil.pageInfo2DTO(PageInfo, BlogCommentDO.class);


    }

    /**
     * 新增博客评论
     *
     * @param blogComment 博客评论
     * @return 结果
     */
    @Override
    public int insertBlogComment(BlogCommentDO blogComment) {
        blogComment.setCreateTime(LocalDateTime.now());
        return dashboardBlogCommentMapper.insertBlogComment(blogComment);
    }

    /**
     * 修改博客评论
     *
     * @param blogComment 博客评论
     * @return 结果
     */
    @Override
    public int updateBlogComment(BlogCommentDO blogComment) {
        blogComment.setUpdateTime(LocalDateTime.now());
        return dashboardBlogCommentMapper.updateBlogComment(blogComment);
    }

    /**
     * 批量删除博客评论
     *
     * @param ids 需要删除的博客评论主键
     * @return 结果
     */
    @Override
    public int deleteBlogCommentByIds(Integer[] ids, int updateUserId) {
        return dashboardBlogCommentMapper.deleteBlogCommentByIds(ids, updateUserId);
    }

    /**
     * 删除博客评论信息
     *
     * @param id 博客评论主键
     * @return 结果
     */
    @Override
    public int deleteBlogCommentById(Integer id) {
        return dashboardBlogCommentMapper.deleteBlogCommentById(id);
    }
}
