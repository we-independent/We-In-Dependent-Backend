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
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.SaveBlogService;
/**
 * @author Elly
 * 2025-04-13
 */
@RestController
@RequestMapping(value = "/api/save-blogs")
public class SaveBlogController {
    
    @Autowired
    private SaveBlogService saveBlogService;

    @PostMapping("/{blogId}")
    public void saveBlog(@PathVariable int blogId, @RequestParam int userId) throws Exception{
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

    @DeleteMapping("/{blogId}")
    public void unsaveBlog(@PathVariable int blogId, @RequestParam int userId) {
        int success;
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

    @GetMapping
    public ResponseEntity<List<BlogArticleDO>> getSavedBlogs(@RequestParam int userId) {
        return ResponseEntity.ok(saveBlogService.getSavedBlogs(userId));
    }
}
