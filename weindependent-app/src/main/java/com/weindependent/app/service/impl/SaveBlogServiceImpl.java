package com.weindependent.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogArticleMapper;
import com.weindependent.app.database.mapper.weindependent.SaveBlogMapper;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.service.SaveBlogService;
/**
 * @author Elly
 * 2025-04-13
 */
public class SaveBlogServiceImpl implements SaveBlogService{
    @Autowired
    private SaveBlogMapper saveBlogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DashboardBlogArticleMapper blogArticleMapper;

    public void saveBlog(int userId, int blogId) {
        if (!blogArticleMapper.existsById(blogId)) {
            throw new IllegalArgumentException("Invalid blogId");
        }
        if (!userMapper.existsById(userId)) {
            throw new IllegalArgumentException("Invalid userId");
        }
        saveBlogMapper.saveBlog(userId, blogId);
    }

    public void unsaveBlog(int userId, int blogId) {
        if (!blogArticleMapper.existsById(blogId)) {
            throw new IllegalArgumentException("Invalid blogId");
        }
        if (!userMapper.existsById(userId)) {
            throw new IllegalArgumentException("Invalid userId");
        }
        saveBlogMapper.unsaveBlog(userId, blogId);
    }

    public List<BlogArticleDO> getSavedBlogs(int userId) {
        if (!userMapper.existsById(userId)) {
            throw new IllegalArgumentException("Invalid userId");
        }
        return saveBlogMapper.getSavedBlogs(userId);
    }   
}
