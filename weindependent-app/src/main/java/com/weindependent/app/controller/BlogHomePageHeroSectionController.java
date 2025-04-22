package com.weindependent.app.controller;

import com.weindependent.app.service.MostSavedService;
import com.weindependent.app.vo.BlogHomePageHeroVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "Blog首页Hero区域")
@RestController
@RequestMapping("/api/blog-hero")
public class BlogHomePageHeroSectionController {

    @Resource
    private MostSavedService mostSavedService;

    // 获取Blog首页Hero区域列表
    @GetMapping("/current")
    public List<BlogHomePageHeroVO> getEditorPickArticles(){
        return mostSavedService.getCurrentBlogHomePageHeroVO();
    }
}
