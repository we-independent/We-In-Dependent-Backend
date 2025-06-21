package com.weindependent.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.weindependent.SaveListMapper;
import com.weindependent.app.database.mapper.weindependent.SaveMapper;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.service.SaveListService;
import com.weindependent.app.service.SaveService;
/**
 * @author Elly
 * 2025-04-17
 */
@Service
public class SaveServiceImpl implements SaveService{
    @Autowired
    private SaveMapper saveMapper;
    @Autowired 
    private SaveListMapper saveListMapper;
    @Autowired
    private SaveListService saveListService;

    /* 收藏文章 */
    @Transactional
    public int saveBlog(int userId, Integer listId, int blogId){
        // if (!saveMapper.existBlogId(blogId)) {
        //     return ErrorCode.BLOG_NOT_EXIST.getCode();
        // }
        if (listId == null) {
            int result =  saveBlogToDefault(userId, blogId);
            saveMapper.updateSaveListMetadata(userId, blogId); //update last_edited time and image for list
            return result;
        }
        else {
            if(!checkListOwnership(userId, listId)) 
                return ErrorCode.UNAUTHORIZED_ACCESS.getCode();
            else {
                int result = saveBlogToList(listId, blogId);
                saveMapper.updateSaveListMetadata(userId, blogId);///update last_edited time and image for list
                return result;
            }
        }
    }
    
    /* 取消收藏文章 */
    @Transactional
    public int unsaveBlog(int userId, Integer listId, int blogId){
        // if (!saveMapper.existBlogId(blogId)) {
        //     return ErrorCode.BLOG_NOT_EXIST.getCode();
        // }
        if (listId == null) return unsaveBlogFromDefault(userId, blogId);
        else {
            if (!checkListOwnership(userId, listId))
                return ErrorCode.UNAUTHORIZED_ACCESS.getCode();
            else return unsaveBlogFromList(listId, blogId);
        }
    }

    public boolean blogIsSaved(int userId, int blogId){
        return saveMapper.blogIsSaved(userId, blogId);
    }

    /* 显示所有收藏的文章 
     * @param query pageNum = ?, pageSize = ?. default page num = 1, size = 9.
    */
    public PageInfo<BlogArticleCardQry>  allSavedArticles(BlogArticleListQry query, int userId){
        int pageNum = (query.getPageNum() != null && query.getPageNum() > 0) ? query.getPageNum() : 1;
        int pageSize = (query.getPageSize() != null && query.getPageSize() > 0) ? query.getPageSize() : 9;
        PageHelper.startPage(pageNum, pageSize);

        List<BlogArticleCardQry> articles = saveMapper.allSavedArticles(userId);
        return new PageInfo<>(articles);
    }

    /* 收藏活动 */
    public int saveEvent(int userId, int eventId){
        if(!saveMapper.existEventId(eventId)){
            return ErrorCode.EVENT_NOT_EXIST.getCode();
        }else if(saveMapper.saveEvent(userId, eventId) <= 0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        else return ErrorCode.SUCCESS.getCode();
    }

    /* 取消活动收藏 */
    public int unsaveEvent(int userId, int eventId){
        if(!saveMapper.existEventId(eventId)){
            return ErrorCode.EVENT_NOT_EXIST.getCode();
        }else if(saveMapper.unsaveEvent(userId, eventId) <= 0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        else return ErrorCode.SUCCESS.getCode();
    }

    /* helper 储存文章到默认收藏夹 */
    private int saveBlogToDefault(int userId, int blogId) {
        Integer list = saveListMapper.findDefaultListByUserId(userId);
        int listId = (list == null)
            ? saveListService.createDefaultList(userId)
            : list;
        if (saveMapper.saveBlog(listId, blogId) <=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /* helper 储存文章到指定收藏夹 */
    private int saveBlogToList(int listId, int blogId){
        if (saveMapper.saveBlog(listId, blogId) <=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /* helper 从默认收藏夹中删除文章 */
    private int unsaveBlogFromDefault(int userId, int blogId) {
        Integer list = saveListMapper.findDefaultListByUserId(userId);
        int listId = list;
        if (saveMapper.unsaveBlog(listId, blogId)<=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /* helper 从指定收藏夹删除 */
    protected int unsaveBlogFromList(int listId, int blogId){
        if (saveMapper.unsaveBlog(listId, blogId)<=0){
            return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
        return ErrorCode.SUCCESS.getCode();
    }

    /* helper 检查user是否拥有某收藏夹 */
    private boolean checkListOwnership(int userId, int listId){
        return saveListMapper.checkListOwnership(userId, listId);
    }
}
