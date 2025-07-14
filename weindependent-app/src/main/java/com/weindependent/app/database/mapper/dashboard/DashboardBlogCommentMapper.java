package com.weindependent.app.database.mapper.dashboard;

import java.util.List;

import com.weindependent.app.database.dataobject.BlogCommentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客评论Mapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Mapper
public interface DashboardBlogCommentMapper {


    /**
     * 查询博客评论列表
     *
     * @param blogComment 博客评论
     * @return 博客评论集合
     */
    List<BlogCommentDO> selectBlogCommentList(BlogCommentDO blogComment);



    /**
     * 批量删除博客评论, 软删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBlogCommentByIds(Integer[] ids, int updateUserId);
}
