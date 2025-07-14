package com.weindependent.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping
    public List<TagDO> getTags(@RequestParam(required = false) String category) throws Exception {
        if (category != null) {
            return tagService.getTagsByCategory(category);
            
        }
        return tagService.getAllTags();
    }

    @Operation(summary = "获取某blog的所有tag")
    @GetMapping("/{blogId}")
    public List<String> getTagsByBlogId(@PathVariable int blogId){
        return tagService.getTagsByArticleId(blogId).stream()
        .filter(tag -> !tag.getIsDeleted()) // 只要 isDeleted == false
        .map(TagDO::getName)              // 提取 name
        .collect(Collectors.toList());
         
    }

}
