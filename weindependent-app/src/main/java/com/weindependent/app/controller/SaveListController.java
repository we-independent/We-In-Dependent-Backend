package com.weindependent.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;
import com.weindependent.app.dto.SaveListQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.SaveListService;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Elly
 * 2025-04-17
 * Note: all need to pass token
 */
@RestController
@RequestMapping(value = "/api/lists")
public class SaveListController {
    @Autowired
    private SaveListService saveListService;

    @Operation(summary = "返回收藏夹下的所有文章")
    @GetMapping("/get-all-articles/{listId}")
    public List<BlogArticleDO> getSavedBlogs(@PathVariable int listId) {
        int userId = StpUtil.getLoginIdAsInt();
        return saveListService.getSavedBlogs(userId, listId);
    }

    @Operation(summary = "根据user id和list name建立新收藏夹")
    @PostMapping("/create-list")
    public void createList(@RequestBody SaveListQry saveBlogQry){
        int userId = StpUtil.getLoginIdAsInt();
        String listName = saveBlogQry.getListName();
        int resultCode = saveListService.createList(userId, listName);
        if (resultCode == 0) return;
        else if (resultCode == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }
    @Operation(summary = "返回用户所有收藏夹")
    @GetMapping("get-all-list")
    public List<SaveListDO> getSavingLists(@RequestBody SaveListQry saveBlogQry) {
        int userId = StpUtil.getLoginIdAsInt();
        return saveListService.getSavingList(userId);
    }

    @Operation(summary = "删除收藏夹")
    @DeleteMapping("/delete-list/{listId}")
    public void deleteList(@PathVariable int listId){
        int userId = StpUtil.getLoginIdAsInt();
        int resultCode = saveListService.deleteList(userId, listId);
        if (resultCode ==ErrorCode.SUCCESS.getCode()) return;
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

    @Operation(summary = "更改收藏夹名字")
    @PostMapping("update-list-name")
    public void setListOrder(@RequestBody SaveListQry saveBlogQry){
        int userId = StpUtil.getLoginIdAsInt();
        int listId = saveBlogQry.getListId();
        String name = saveBlogQry.getListName();
        int resultCode = saveListService.updateListName(userId, listId, name);
        if (resultCode ==ErrorCode.SUCCESS.getCode()) return;
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

}
