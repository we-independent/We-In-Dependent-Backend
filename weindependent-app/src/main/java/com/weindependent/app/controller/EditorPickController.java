package com.weindependent.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weindependent.app.service.EditorPickService;
import com.weindependent.app.vo.EditorPickVO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/editor-picks")
public class EditorPickController {
    
    @Resource
    private EditorPickService editorPickService;

    // 获取编辑精选文章列表
    @GetMapping   
    public Map<String, Object> getEditorPickArticles(
        @RequestParam(value="limit", defaultValue="2") Integer limit){
        Map<String, Object> result = new HashMap<>();

        try {
            List<EditorPickVO> articles = editorPickService.getEditorPickArticles(limit);

            result.put("code", 200);
            result.put("message", "success");
            result.put("data", articles);

        } catch (Exception e){
            result.put("code", 500);
            result.put("message", "Failed to get editor's picks articles: " + e.getMessage());
        }
        return result;
    }
        
    //将文章标记为编辑精选
    @PostMapping("/{articleId}")
    public ResponseEntity<Map<String, Object>> addEditorPickArticle(
        @PathVariable Integer articleId,
        @RequestParam(defaultValue = "1") Integer userId) {
    
        Map<String, Object> result = new HashMap<>();
    
        try {
            boolean success = editorPickService.addEditorPickArticle(articleId, userId);
    
            if (!success) {
                result.put("code", 400);
                result.put("message", "文章不存在、未发布或已删除，无法推送到 Editor's Pick");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
    
            result.put("code", 200);
            result.put("message", "推送成功");
            return ResponseEntity.ok(result);
    
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "系统异常: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
        
        //取消文章的编辑精选标记
        @DeleteMapping("/{articleId}")
        public Map<String, Object> removeEditorPickArticle(
        @PathVariable Integer articleId){
            Map<String, Object> result = new HashMap<>();
        
            try {
                boolean success = editorPickService.removeEditorPickArticle(articleId);

                if (success){
                    result.put("code", 200);
                    result.put("message", "Removed successfully");
                } else {
                    result.put("code", 400);
                    result.put("message", "Removal failed, the article may not be marked as an editor's pick");
                }
            } catch(Exception e){
                result.put("code", 500);
                result.put("message", "Failed to remove from editor picks: " + e.getMessage());
            }

            return result;
        }

        //检查文章是否为编辑精选
        @GetMapping("/{articleId}/check")
        public Map<String, Object> isEditorPickArticle(@PathVariable Integer articleId) {
            Map<String, Object> result = new HashMap<>();

            try {
                boolean isEditorPick = editorPickService.isEditorPickArticle(articleId);

                result.put("code", 200);
                result.put("message", "success");
                result.put("data", isEditorPick);
            } catch (Exception e){
                result.put("code", 500);
                result.put("message", "Failed to check the status: " + e.getMessage());
            }
            
            return result;
        }
        


}