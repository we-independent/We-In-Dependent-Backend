package com.weindependent.app.controller;

import com.weindependent.app.service.MostSavedService;
import com.weindependent.app.vo.EditorPickVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blog-hero/most-saved")
public class MostSavedController {

    @Resource
    private MostSavedService mostSavedService;

    // 获取编辑精选文章列表
    @GetMapping()
    public Map<String, Object> getEditorPickArticles(){
        Map<String, Object> result = new HashMap<>();


        try {
            List<EditorPickVO> articles = mostSavedService.getCurrentMostSavedArticleVO();

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", articles);

        } catch (Exception e){
            result.put("code", 500);
            result.put("message", "Failed to get editor's picks articles: " + e.getMessage());
        }
        return result;
    }
}
