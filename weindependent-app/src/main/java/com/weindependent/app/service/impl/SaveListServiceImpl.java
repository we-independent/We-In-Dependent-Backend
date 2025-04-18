package com.weindependent.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;
import com.weindependent.app.database.mapper.weindependent.SaveListMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.SaveListService;

@Service
public class SaveListServiceImpl implements SaveListService{
    @Autowired 
    private SaveListMapper saveListMapper;

    /*获取某收藏夹中所有文章 */
    public List<BlogArticleDO> getSavedBlogs(int userId, int listId) {
        if (!checkListOwnership(userId, listId)) 
            throw new ResponseException(ErrorCode.UNAUTHORIZED_ACCESS.getCode(), ErrorCode.UNAUTHORIZED_ACCESS.getTitle());
        else return saveListMapper.getSavedBlogsFromList(listId);
    }   
    /*获取所有收藏夹 */
    public List<SaveListDO>getSavingList(int userId){
        return saveListMapper.getSavedList(userId);
    }
    /*创建收藏夹*/
    public int createList(int userId, String listName){
        if (createList(userId, listName, false) > 0) return ErrorCode.SUCCESS.getCode();
        else return ErrorCode.UPDATE_DB_FAILED.getCode();
    }

    public int createDefaultList(int userId){
        int listId = createList(userId, "Saved Articles(default)", true);
        if ( listId > 0) return listId;
        else return ErrorCode.UPDATE_DB_FAILED.getCode();
    }

    /*删除收藏夹 */
    public int deleteList(int userId, int listId){
        if (!checkListOwnership(userId, listId)) 
            return ErrorCode.UNAUTHORIZED_ACCESS.getCode();
        else {
            if (saveListMapper.deleteList(userId, listId)>0) return ErrorCode.SUCCESS.getCode();
            else return ErrorCode.UPDATE_DB_FAILED.getCode();
        }
    }

    /*helper返回值为新建list id */
    private int createList(int userId, String listName, boolean isDefault){
        SaveListDO list = new SaveListDO();
        list.setIsDefault(isDefault);
        list.setName(listName);
        list.setUserId(userId);
        saveListMapper.createList(list);
        return list.getListId();
    }

    /* helper 检查user是否拥有某收藏夹 */
    private boolean checkListOwnership(int userId, int listId){
        return saveListMapper.checkListOwnership(userId, listId) > 0;
    }
}
