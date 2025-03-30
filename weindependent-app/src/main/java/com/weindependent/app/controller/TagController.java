package com.weindependent.app.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.service.TagService;


@RestController
@RequestMapping(value = "/tag")
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
}
