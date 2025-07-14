package com.weindependent.app.service.impl.dashboard;

import java.util.List;
import cn.dev33.satoken.stp.StpUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogCommentMapper;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogCommentQry;
import com.weindependent.app.utils.PageInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IDashboardBlogCommentService;

/**
 * 博客评论Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Service
public class DashboardBlogCommentServiceImpl implements IDashboardBlogCommentService {
    @Autowired
    private DashboardBlogCommentMapper dashboardBlogCommentMapper;


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
     * 批量删除博客评论
     *
     * @param ids 需要删除的博客评论主键
     * @return 结果
     */
    @Override
    public int deleteBlogCommentByIds(Integer[] ids) {
        int updateUserId = StpUtil.getLoginIdAsInt();
        return dashboardBlogCommentMapper.deleteBlogCommentByIds(ids, updateUserId);
    }


}
