package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.dto.BlogCommentQry;


/**
 * 博客评论Service接口
 *
 * @author christina
 * 2025-03-23
 */
public interface IDashboardBlogCommentService {


    /**
     * 查询博客评论列表
     *
     * @param blogCommentQry 博客评论查找模板
     * @return 博客评论集合
     */
    PageInfo<BlogCommentDO> selectBlogCommentList(BlogCommentQry blogCommentQry);



    /**
     * 批量删除博客评论
     *
     * @param ids 需要删除的博客评论主键集合
     * @return 结果
     */
    int deleteBlogCommentByIds(Integer[] ids);


}
