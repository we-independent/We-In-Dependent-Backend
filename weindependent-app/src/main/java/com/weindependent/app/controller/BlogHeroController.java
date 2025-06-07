package com.weindependent.app.controller;

import com.weindependent.app.service.BlogHeroService;
import com.weindependent.app.vo.websiteoperation.BlogHeroVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Blog首页Hero区域")
@RestController
@RequestMapping("/api/blog-hero")
public class BlogHeroController{


    @Autowired
    private BlogHeroService blogHeroService;

    // 获取Blog首页Hero区域列表
    @GetMapping("/current")
    public List<BlogHeroVO> getCurrentHeroArticles(){
        return blogHeroService.getCurrentBlogHeroVOList();
    }
}
