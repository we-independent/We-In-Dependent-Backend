package com.weindependent.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.dto.BlogArticleCardQry;
import com.weindependent.app.dto.BlogArticleListQry;
import com.weindependent.app.dto.SaveListQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.SaveService;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
/**
 * @author Elly
 * @apiNote all under api/saves must have token in request header
 * 2025-04-17
 */
@RestController
@RequestMapping(value = "/api/saves")
public class SaveController {
    
    @Autowired
    private SaveService saveService;

    /**
     * Save an article. token should be passed through header
     * @param blogId placed on url path.
     * @param saveBlogQry optionally contains a listId, if user want to save a blog into a specific list
     * @throws Exception
     */
    @Operation(summary = "将文章储存到收藏夹")
    @PostMapping("/blogs/{blogId}")
    public void saveBlog(@PathVariable int blogId, @RequestBody SaveListQry saveBlogQry) throws Exception{
        int userId = 145;//StpUtil.getLoginIdAsInt();
        int resultCode = saveService.saveBlog(userId, saveBlogQry.getListId(), blogId);
        
        if (resultCode == ErrorCode.SUCCESS.getCode()) return;
        else if (resultCode == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.BLOG_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.BLOG_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
        else if (resultCode == ErrorCode.UNAUTHORIZED_ACCESS.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UNAUTHORIZED_ACCESS.getTitle());
        }
    }

    /**
     * Similar to save an article. Token must be in request header.
     * @param blogId
     * @param saveBlogQry
     */
    @Operation(summary = "将文章从收藏夹删除")
    @DeleteMapping("/blogs/{blogId}")
    public void unsaveBlog(@PathVariable int blogId, @RequestBody SaveListQry saveBlogQry) {
        int userId = 145;//StpUtil.getLoginIdAsInt();
        int resultCode = saveService.unsaveBlog(userId, saveBlogQry.getListId(), blogId);
        if (resultCode == ErrorCode.SUCCESS.getCode()) return;
        else if (resultCode == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.BLOG_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.BLOG_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

    /** 显示所有收藏的文章 ordered by saved time descendent
     * @param query pageNum = ?, pageSize = ?. default page num = 1, size = 9.
     * @return PageInfo with data match with frontend blog card
    */
    @Operation(summary = "显示用户收藏的所有文章(不论list)")
    @PostMapping("/all-saved-articles")
    public PageInfo<BlogArticleCardQry> allSavedArticles(@RequestBody BlogArticleListQry query){
        int userId =145;//StpUtil.getLoginIdAsInt();
        PageInfo<BlogArticleCardQry> result = saveService.allSavedArticles(query, userId);
        return result;
    }

    @Operation(summary = "用户登录后检查blog是否被该用户收藏")
    @GetMapping("/blog-is-saved/{blogId}")
    public boolean blogIsSaved(@PathVariable int blogId){
        int userId = StpUtil.getLoginIdAsInt();
        return saveService.blogIsSaved(userId, blogId);
    }


    /**
     * Save an event. Token must be in request header.
     * @param eventId
     */
    @Operation(summary = "收藏活动")
    @PostMapping("/events/{eventId}")
    public void saveEvent(@PathVariable int eventId){
        int userId = StpUtil.getLoginIdAsInt();
        int resultCode = saveService.saveEvent(userId, eventId);
        if (resultCode == ErrorCode.SUCCESS.getCode()) return;
        else if (resultCode == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.BLOG_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.BLOG_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

    @Operation(summary = "取消活动收藏")
    @DeleteMapping("/events/{eventId}")
    public void unsaveEvent(@PathVariable int eventId){
        int userId = StpUtil.getLoginIdAsInt();
        int resultCode = saveService.unsaveEvent(userId, eventId);
        if (resultCode == ErrorCode.SUCCESS.getCode()) return;
        else if (resultCode == ErrorCode.USER_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.USER_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.BLOG_NOT_EXIST.getCode()){
            throw new ResponseException(resultCode, ErrorCode.BLOG_NOT_EXIST.getTitle());
        }
        else if (resultCode == ErrorCode.UPDATE_DB_FAILED.getCode()){
            throw new ResponseException(resultCode, ErrorCode.UPDATE_DB_FAILED.getTitle());
        }
    }

    

}
