package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogCommentQry;

/**
 * 博客评论Service接口
 * 
 * @author christina
 *    2025-03-23
 */
public interface IBlogCommentService 
{
    /**
     * 查询博客评论
     * 
     * @param id 博客评论主键
     * @return 博客评论
     */
    public BlogCommentDO selectBlogCommentById(Integer id);

    /**
     * 查询博客评论列表
     * 
     * @param blogCommentQry 博客评论查找模板
     * @return 博客评论集合
     */
    PageInfo<BlogCommentDO> selectBlogCommentList(BlogCommentQry blogCommentQry);

    /**
     * 新增博客评论
     * 
     * @param blogComment 博客评论
     * @return 结果
     */
    public int insertBlogComment(BlogCommentDO blogComment);

    /**
     * 修改博客评论
     * 
     * @param blogComment 博客评论
     * @return 结果
     */
    public int updateBlogComment(BlogCommentDO blogComment);

    /**
     * 批量删除博客评论
     * 
     * @param ids 需要删除的博客评论主键集合
     * @return 结果
     */
    public int deleteBlogCommentByIds(Integer[] ids);

    /**
     * 删除博客评论信息
     * 
     * @param id 博客评论主键
     * @return 结果
     */
    public int deleteBlogCommentById(Integer id);
}
