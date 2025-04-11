package com.weindependent.app;

import com.weindependent.app.database.mapper.weindependent.EditorPickMapper;
import com.weindependent.app.database.mapper.weindependent.BlogArticleMapper;
import com.weindependent.app.database.dataobject.EditorPickDO;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.service.impl.EditorPickServiceImpl;
import com.weindependent.app.vo.EditorPickVO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EditorPickServiceTest {

    @Mock
    private EditorPickMapper editorPickMapper;
    
    @Mock
    private BlogArticleMapper blogArticleMapper;

    @InjectMocks
    private EditorPickServiceImpl editorPickService;

    @Test
    public void testGetEditorPickArticles() {
       
        List<Integer> mockArticleIds = Arrays.asList(10, 20, 30, 40, 50);

        List<BlogArticleDO> mockArticles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BlogArticleDO article = new BlogArticleDO();
            article.setId(mockArticleIds.get(i));
            article.setTitle("Article " + (i + 1));
            article.setSummary("Summary of article " + (i + 1));
            article.setCategoryId(1);
            article.setAuthorId(200 + i);
            mockArticles.add(article);
        }

        // Mock editorPickMapper查询编辑精选文章ID列表
        when(editorPickMapper.findEditorPickArticleIds(anyInt())).thenReturn(mockArticleIds);
        
        // Mock blogArticleMapper查询文章详情
        when(blogArticleMapper.findByIds(anyList())).thenReturn(mockArticles);

        List<EditorPickVO> editorPickVOs = editorPickService.getEditorPickArticles(5);

        assertNotNull(editorPickVOs);
        assertEquals(5, editorPickVOs.size());
    }

    @Test
    public void testGetEditorPickArticlesWithEmptyResult() {
        // 模拟没有编辑精选文章的情况
        when(editorPickMapper.findEditorPickArticleIds(anyInt())).thenReturn(new ArrayList<>());
        
        List<EditorPickVO> editorPickVOs = editorPickService.getEditorPickArticles(5);
        
        assertNotNull(editorPickVOs);
        assertTrue(editorPickVOs.isEmpty());
    }

    @Test
    public void testAddEditorPickArticle() {
        // 创建模拟文章数据
        BlogArticleDO article = new BlogArticleDO();
        article.setId(123);
        article.setTitle("Test Article");
        article.setArticleStatus("published");
        article.setIsDeleted(0);
        
        // Mock editorPickMapper检查文章是否已经是编辑精选，返回false表示不是
        when(editorPickMapper.isEditorPick(123)).thenReturn(false);
        
        when(blogArticleMapper.findById(123)).thenReturn(article);
        
        when(editorPickMapper.insert(any(EditorPickDO.class))).thenReturn(1);
    
        boolean result = editorPickService.addEditorPickArticle(123, 456);
    
        assertTrue(result);
        
        // 使用argThat进行更详细的验证
        Mockito.verify(editorPickMapper).insert(Mockito.argThat(editorPick -> 
            editorPick.getArticleId() == 123 && 
            editorPick.getCreateUserId() == 456 && 
            editorPick.getStatus() == 1 // 验证状态被设置为有效
        ));
    }
    
    @Test
    public void testAddEditorPickArticleAlreadyPicked() {
        // Mock editorPickMapper检查文章是否已经是编辑精选，返回true表示已经是
        when(editorPickMapper.isEditorPick(123)).thenReturn(true);
        
        boolean result = editorPickService.addEditorPickArticle(123, 456);
        
        assertTrue(result);
        
        Mockito.verify(editorPickMapper, Mockito.never()).insert(any(EditorPickDO.class));
    }
    
    @Test
    public void testAddEditorPickArticleNotPublished() {
        // 创建模拟文章数据 - 未发布状态
        BlogArticleDO article = new BlogArticleDO();
        article.setId(123);
        article.setTitle("Test Article");
        article.setArticleStatus("draft"); // 非published状态
        article.setIsDeleted(0);
        
        // Mock editorPickMapper检查文章是否已经是编辑精选
        when(editorPickMapper.isEditorPick(123)).thenReturn(false);
        
        when(blogArticleMapper.findById(123)).thenReturn(article);
        
        boolean result = editorPickService.addEditorPickArticle(123, 456);
        
        assertFalse(result);
    }
    
    @Test
    public void testAddEditorPickArticleDeleted() {
        // 创建模拟文章数据 - 已删除状态
        BlogArticleDO article = new BlogArticleDO();
        article.setId(123);
        article.setTitle("Test Article");
        article.setArticleStatus("published");
        article.setIsDeleted(1); 
        
        // Mock editorPickMapper检查文章是否已经是编辑精选
        when(editorPickMapper.isEditorPick(123)).thenReturn(false);
        
        when(blogArticleMapper.findById(123)).thenReturn(article);
        
        boolean result = editorPickService.addEditorPickArticle(123, 456);
        
        assertFalse(result);
    }

    @Test
    public void testRemoveEditorPickArticle() {
        // Mock editorPickMapper的更新状态方法
        when(editorPickMapper.updateStatusByArticleId(123, 0)).thenReturn(1);
    
        boolean result = editorPickService.removeEditorPickArticle(123);
    
        assertTrue(result);
        
        // 验证调用的是状态更新方法，而不是物理删除方法
        Mockito.verify(editorPickMapper).updateStatusByArticleId(123, 0);
    }
    
    @Test
    public void testRemoveEditorPickArticleNotExist() {
        // Mock editorPickMapper的更新状态方法，返回0表示没有更新任何记录
        when(editorPickMapper.updateStatusByArticleId(123, 0)).thenReturn(0);
    
        boolean result = editorPickService.removeEditorPickArticle(123);
    
        assertFalse(result);
    }
    @Test
    public void testIsEditorPickArticle() {
        // Mock editorPickMapper的isEditorPick方法
        when(editorPickMapper.isEditorPick(123)).thenReturn(true);
        when(editorPickMapper.isEditorPick(456)).thenReturn(false);
        
        assertTrue(editorPickService.isEditorPickArticle(123));
        assertFalse(editorPickService.isEditorPickArticle(456));
    }
}