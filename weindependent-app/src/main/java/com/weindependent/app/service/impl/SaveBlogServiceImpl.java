package com.weindependent.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogArticleMapper;
import com.weindependent.app.database.mapper.weindependent.SaveBlogMapper;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.service.SaveBlogService;
/**
 * @author Elly
 * 2025-04-13
 */
@Service
public class SaveBlogServiceImpl implements SaveBlogService{
    @Autowired
    private SaveBlogMapper saveBlogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DashboardBlogArticleMapper blogArticleMapper;

    public int saveBlog(int userId, int blogId) {
        if (!blogArticleMapper.existsById(blogId)) {
            return ErrorCode.BLOG_NOT_EXIST.getCode();
        }
        if (!userMapper.existsById(userId)) {
            return ErrorCode.USER_NOT_EXIST.getCode();
        }
        if (saveBlogMapper.saveBlog(userId, blogId) <=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return 0;
    }

    public int unsaveBlog(int userId, int blogId) {
        if (!blogArticleMapper.existsById(blogId)) {
            return ErrorCode.BLOG_NOT_EXIST.getCode();
        }
        if (!userMapper.existsById(userId)) {
            return ErrorCode.USER_NOT_EXIST.getCode();
        }
        if (saveBlogMapper.unsaveBlog(userId, blogId)<=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return 0;
    }

    public List<BlogArticleDO> getSavedBlogs(int userId) {
        if (!userMapper.existsById(userId)) {
            throw new IllegalArgumentException("Invalid userId");
        }
        return saveBlogMapper.getSavedBlogs(userId);
    }   
}
