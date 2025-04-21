package com.weindependent.app.controller;

import com.weindependent.app.service.MostSavedService;
import com.weindependent.app.vo.BlogHomePageHeroVO;
import com.weindependent.app.vo.EditorPickVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blog-hero")
public class MostSavedController {

    @Resource
    private MostSavedService mostSavedService;

    // 获取Blog首页Hero区域列表
    @GetMapping("/current")
    public List<BlogHomePageHeroVO> getCurrentHeroArticles(){
        return mostSavedService.getCurrentBlogHomePageHeroVO();
    }
}
