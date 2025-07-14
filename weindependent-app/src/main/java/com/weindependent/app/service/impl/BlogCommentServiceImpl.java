package com.weindependent.app.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.weindependent.app.database.dataobject.BlogCommentDO;
import com.weindependent.app.database.mapper.weindependent.BlogCommentMapper;
import com.weindependent.app.service.IBlogCommentService;
import com.weindependent.app.vo.blog.BlogCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogCommentServiceImpl implements IBlogCommentService {

    @Autowired
    private BlogCommentMapper blogCommentMapper;

    /**
     * 查询博客评论列表
     *
     * @param articleId 博客文章Id
     * @return 博客评论集合
     */
    @Override
    public List<BlogCommentVO> selectBlogCommentVOListByArticleId(Integer articleId) {
       if(StpUtil.isLogin()) {
           return blogCommentMapper.selectBlogCommentVOListByArticleId(articleId,StpUtil.getLoginIdAsInt());
       }else{
           return blogCommentMapper.selectBlogCommentVOListByArticleId(articleId);
       }
    }

    /**
     * 新增博客评论
     *
     * @param blogComment 博客评论
     * @return 结果
     */
    @Override
    public int insertBlogComment(BlogCommentDO blogComment) {
        int userId = StpUtil.getLoginIdAsInt();
        blogComment.setCreateUserId(userId);
        blogComment.setUpdateUserId(userId);
        blogComment.setCommentAuthorUserId(userId);
        blogComment.setCreateTime(LocalDateTime.now());
        return blogCommentMapper.insertBlogComment(blogComment);
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
        int userId = StpUtil.getLoginIdAsInt();
        blogComment.setUpdateUserId(userId);
        return blogCommentMapper.updateBlogComment(blogComment);
    }

    /**
     * 批量删除博客评论
     *
     * @param ids          需要删除的博客评论主键集合
     * @return 结果
     */
    @Override
    public int deleteBlogCommentByIds(Integer[] ids) {
        int updateUserId = StpUtil.getLoginIdAsInt();
        return blogCommentMapper.deleteBlogCommentByIds(ids, updateUserId);
    }
}
