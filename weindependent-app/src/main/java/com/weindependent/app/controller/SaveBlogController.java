package com.weindependent.app.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.dto.SaveBlogQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.SaveBlogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import cn.dev33.satoken.stp.StpUtil;
/**
 * @author Elly
 * 2025-04-13
 */
@RestController
@RequestMapping(value = "/api/blogs")
public class SaveBlogController {
    
    @Autowired
    private SaveBlogService saveBlogService;

    @Operation(summary = "根据blog id和user id将文章储存到默认收藏夹")
    @PostMapping("save/{blogId}")
    public void saveBlog(@PathVariable int blogId) throws Exception{
        int userId = StpUtil.getLoginIdAsInt();
        int success;
        success = saveBlogService.saveBlog(userId, blogId);
        if (success >=0) return;
        else if (success == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (success == ErrorCode.BLOG_NOT_EXIST.getCode()){
            throw new ResponseException(ErrorCode.BLOG_NOT_EXIST.getCode(), ErrorCode.BLOG_NOT_EXIST.getTitle());
        }
        else if (success == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

    @Operation(summary = "根据blog id和user id将文章从默认收藏夹删除")
    @DeleteMapping("unsave/{blogId}")
    public void unsaveBlog(@PathVariable int blogId) {
        int success;
        int userId = StpUtil.getLoginIdAsInt();
        success = saveBlogService.unsaveBlog(userId, blogId);
        if (success >=0) return;
        else if (success == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (success == ErrorCode.BLOG_NOT_EXIST.getCode()){
            throw new ResponseException(ErrorCode.BLOG_NOT_EXIST.getCode(), ErrorCode.BLOG_NOT_EXIST.getTitle());
        }
        else if (success == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

    @Operation(summary = "根据user id返回该用户收藏的所有文章")
    @GetMapping
    public ResponseEntity<List<BlogArticleDO>> getSavedBlogs() {
        int userId = StpUtil.getLoginIdAsInt();
        return ResponseEntity.ok(saveBlogService.getSavedBlogs(userId));
    }

    @Operation(summary = "根据user id和新收藏夹名建立新收藏夹")
    @PostMapping("create-list/{userId}")
    public void createList(@RequestBody SaveBlogQry saveBlogQry){
        int userId = StpUtil.getLoginIdAsInt();
        String listName = saveBlogQry.getListName();
        int success;
        success = saveBlogService.createList(userId, listName);
        if (success >= 0) return;
        else if (success == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(ErrorCode.USER_NOT_EXIST.getCode(), ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (success == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }
}
