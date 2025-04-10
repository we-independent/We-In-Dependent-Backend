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
    public ResponseEntity<?> saveBlog(@PathVariable int blogId, @RequestParam int userId) {
        saveBlogService.saveBlog(userId, blogId);
        return ResponseEntity.ok("Saved");
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<?> unsaveBlog(@PathVariable int blogId, @RequestParam int userId) {
        saveBlogService.unsaveBlog(userId, blogId);
        return ResponseEntity.ok("Un-saved");
    }

    @GetMapping
    public ResponseEntity<List<BlogArticleDO>> getSavedBlogs(@RequestParam int userId) {
        return ResponseEntity.ok(saveBlogService.getSavedBlogs(userId));
    }
}
