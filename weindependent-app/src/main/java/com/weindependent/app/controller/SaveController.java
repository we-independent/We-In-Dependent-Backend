package com.weindependent.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.SaveListDO;
import com.weindependent.app.dto.SaveListQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.SaveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import cn.dev33.satoken.stp.StpUtil;
/**
 * @author Elly
 * 2025-04-17
 * Note: all need to pass token
 */
@RestController
@RequestMapping(value = "/api/saves")
public class SaveController {
    
    @Autowired
    private SaveService saveService;

    @Operation(summary = "将文章储存到收藏夹")
    @PostMapping("/blogs/{blogId}")
    public void saveBlog(@PathVariable int blogId, @RequestBody SaveListQry saveBlogQry) throws Exception{
        int userId = StpUtil.getLoginIdAsInt();
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

    @Operation(summary = "将文章从收藏夹删除")
    @DeleteMapping("/blogs/{blogId}")
    public void unsaveBlog(@PathVariable int blogId, @RequestBody SaveListQry saveBlogQry) {
        int userId = StpUtil.getLoginIdAsInt();
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

    

}
