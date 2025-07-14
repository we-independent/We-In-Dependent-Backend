package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.vo.blog.BlogCommentVO;

import java.util.List;


public interface  BlogCommentMapper {
    /**
     * 查询博客评论
     *
     * @param articleId 博客主键
     * @return 博客评论
     */
  List<BlogCommentVO> selectBlogCommentVOListByArticleId(Integer articleId);

  /**
   * 查询博客评论
   *
   * @param articleId 博客主键
   * @return 博客评论
   */
  List<BlogCommentVO> selectBlogCommentVOListByArticleId(Integer articleId, Integer authorId);


  BlogCommentVO selectBlogCommentVOByCommentId(Integer id);


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
     * 删除博客评论, 软删除
     *
     * @param id 博客评论主键
     * @return 结果
     */
    int deleteBlogCommentById(Integer id);

    /**
     * 批量删除博客评论, 软删除
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBlogCommentByIds(Integer[] ids, int updateUserId);
}
