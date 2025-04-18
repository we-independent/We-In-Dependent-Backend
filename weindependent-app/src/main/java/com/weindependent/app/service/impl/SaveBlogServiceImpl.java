package com.weindependent.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogArticleMapper;
import com.weindependent.app.database.mapper.weindependent.SaveBlogMapper;
import com.weindependent.app.database.mapper.weindependent.UserMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.SaveBlogService;
/**
 * @author Elly
 * 2025-04-17
 */
@Service
public class SaveBlogServiceImpl implements SaveBlogService{
    @Autowired
    private SaveBlogMapper saveBlogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DashboardBlogArticleMapper blogArticleMapper;

    /*收藏文章 */
    public int saveBlog(int userId, Integer listId, int blogId){
        if (!blogArticleMapper.existsById(blogId)) {
            return ErrorCode.BLOG_NOT_EXIST.getCode();
        }
        if (!userMapper.existsById(userId)) {
            return ErrorCode.USER_NOT_EXIST.getCode();
        }
        if (listId == null) return saveBlogToDefault(userId, blogId);
        else {
            if(!checkListOwnership(userId, listId)) 
                return ErrorCode.UNAUTHORIZED_ACCESS.getCode();
            else return saveBlogToList(userId, blogId);
        }
    }
    
    /*取消收藏*/
    public int unsaveBlog(int userId, Integer listId, int blogId){
        if (!blogArticleMapper.existsById(blogId)) {
            return ErrorCode.BLOG_NOT_EXIST.getCode();
        }
        if (!userMapper.existsById(userId)) {
            return ErrorCode.USER_NOT_EXIST.getCode();
        }
        if (listId == null) return unsaveBlogFromDefault(userId, blogId);
        else {
            if (!checkListOwnership(userId, listId))
                return ErrorCode.UNAUTHORIZED_ACCESS.getCode();
            else return unsaveBlogFromList(userId, blogId);
        }
    }

    /*获取某收藏夹中所有文章 */
    public List<BlogArticleDO> getSavedBlogs(int userId, int listId) {
        if (!checkListOwnership(userId, listId)) 
            throw new ResponseException(ErrorCode.UNAUTHORIZED_ACCESS.getCode(), ErrorCode.UNAUTHORIZED_ACCESS.getTitle());
        else return saveBlogMapper.getSavedBlogsFromList(listId);
    }   
    /*获取所有收藏夹 */
    public List<SaveListDO>getSavingList(int userId){
        return saveBlogMapper.getSavedList(userId);
    }
    /*创建收藏夹*/
    public int createList(int userId, String listName){
        if (createList(userId, listName, false) > 0) return ErrorCode.SUCCESS.getCode();
        else return ErrorCode.UPDATE_DB_FAILED.getCode();
    }

    /* helper 储存文章到默认收藏夹 */
    private int saveBlogToDefault(int userId, int blogId) {
        SaveListDO list = saveBlogMapper.findDefaultListByUserId(userId);
        int listId = (list == null)
            ? createList(userId, "Saved Articles(default)", true)
            : list.getListId();
        if (saveBlogMapper.saveBlog(listId, blogId) <=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /* helper 储存文章到指定收藏夹 */
    private int saveBlogToList(int listId, int blogId){
        if (saveBlogMapper.saveBlog(listId, blogId) <=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /* helper 从默认收藏夹中删除文章 */
    private int unsaveBlogFromDefault(int userId, int blogId) {
        SaveListDO list = saveBlogMapper.findDefaultListByUserId(userId);
        int listId = list.getListId();
        if (saveBlogMapper.unsaveBlog(listId, blogId)<=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /* helper 从指定收藏夹删除 */
    private int unsaveBlogFromList(int listId, int blogId){
        if (saveBlogMapper.unsaveBlog(listId, blogId)<=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /*helper返回值为新建list id */
    private int createList(int userId, String listName, boolean isDefault){
        SaveListDO list = new SaveListDO();
        list.setIsDefault(isDefault);
        list.setName(listName);
        list.setUserId(userId);
        saveBlogMapper.createList(list);
        return list.getListId();
    }

    /* helper 检查user是否拥有某收藏夹 */
    private boolean checkListOwnership(int userId, int listId){
        return saveBlogMapper.checkListOwnership(userId, listId) > 0;
    }
}
