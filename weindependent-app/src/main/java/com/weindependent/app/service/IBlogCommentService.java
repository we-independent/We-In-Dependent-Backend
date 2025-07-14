package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.vo.blog.BlogCommentVO;

import java.util.List;

/**
 * 博客评论Service接口
 *
 * @author christina
 * 2025-03-23
 */
public interface IBlogCommentService {



    /**
     * 查询博客评论列表
     *
     * @param articleId 博客文章Id
     * @return 博客评论集合
     */
    List<BlogCommentVO> selectBlogCommentVOListByArticleId(Integer articleId);

    /**
     * 新增博客评论
     *
     * @param blogComment 博客评论
     * @return 结果
     */
    int insertBlogComment(BlogCommentDO blogComment);

    /**
     * 修改博客评论
     *
     * @param blogComment 博客评论
     * @return 结果
     */
    int updateBlogComment(BlogCommentDO blogComment);

    /**
     * 批量删除博客评论
     *
     * @param ids 需要删除的博客评论主键集合
     * @return 结果
     */
    int deleteBlogCommentByIds(Integer[] ids);
}
