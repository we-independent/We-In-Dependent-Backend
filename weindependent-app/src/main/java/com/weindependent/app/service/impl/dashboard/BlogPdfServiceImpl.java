package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.File;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.CategoryDO;
import com.weindependent.app.database.mapper.dashboard.BlogArticleMapper;
import com.weindependent.app.database.mapper.dashboard.BlogPdfMapper;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.dto.BlogPdfQry;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.utils.HtmlSanitizerUtil;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;


import io.swagger.v3.oas.models.media.Content;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogPdfService;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * 博客文章pdfService业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Service
public class BlogPdfServiceImpl implements IBlogPdfService 
{
    @Autowired
    private BlogPdfMapper blogPdfMapper;

    @Autowired
    private BlogArticleMapper blogArticleMapper;  // 用于查询博客正文内容

    @Autowired
    private TemplateEngine templateEngine;  // Thymeleaf 渲染模板引擎
    /**
     * 查询博客文章pdf
     * 
     * @param id 博客文章pdf主键
     * @return 博客文章pdf
     */
    @Override
    public BlogPdfDO selectBlogPdfById(Integer id)
    {
        return blogPdfMapper.selectBlogPdfById(id);
    }

    /**
     * 查询博客文章pdf列表
     * 
     * @param blogPdfQry 博客文章pdf查询模板
     * @return 博客文章pdf
     */
    @Override
    public PageInfo<BlogPdfDO> selectBlogPdfList(BlogPdfQry blogPdfQry)
    {
        BlogPdfDO blogPdfDO = new BlogPdfDO();
        BeanUtils.copyProperties(blogPdfQry, blogPdfDO);
        List<BlogPdfDO> list = blogPdfMapper.selectBlogPdfList(blogPdfDO);
        PageInfo<BlogPdfDO> pageInfo = new PageInfo<>(list);
        return PageInfoUtil.pageInfo2DTO(pageInfo,BlogPdfDO.class);
    }

    /**
     * 新增博客文章pdf
     * 
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    @Override
    public int insertBlogPdf(BlogPdfDO blogPdf)
    {
        blogPdf.setCreateTime(LocalDateTime.now());
        return blogPdfMapper.insertBlogPdf(blogPdf);
    }

    /**
     * 修改博客文章pdf
     * 
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    @Override
    public int updateBlogPdf(BlogPdfDO blogPdf)
    {
        blogPdf.setUpdateTime(LocalDateTime.now());
        return blogPdfMapper.updateBlogPdf(blogPdf);
    }

    /**
     * 批量删除博客文章pdf
     * 
     * @param ids 需要删除的博客文章pdf主键
     * @return 结果
     */
    @Override
    public int deleteBlogPdfByIds(Integer[] ids)
    {
        return blogPdfMapper.deleteBlogPdfByIds(ids);
    }

    /**
     * 删除博客文章pdf信息
     * 
     * @param id 博客文章pdf主键
     * @return 结果
     * 
     */
    @Override
    public int deleteBlogPdfById(Integer id)
    {
        return blogPdfMapper.deleteBlogPdfById(id);
    }

    /**
     * 根据博客 ID 生成 PDF 并返回为 byte[]
     *
     * @param blogId 博客文章主键 id
     * @return PDF 文件的字节数组（byte[]）
     * @author Hurely
     * @since  2025-04-2
     */
    public byte[] generatePdf(Integer id){

        // 1. 查询博客正文内容
        BlogArticleDO blog = blogArticleMapper.selectBlogArticleById(id);
        if(blog == null){
            throw new RuntimeException("Blog not found. ID=" + id);
        }


        // 2. 准备 Thymeleaf 模板上下文，注入变量
        Context context = new Context();
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        context.setVariable("title", blog.getTitle());
        context.setVariable("summary", blog.getSummary());
        context.setVariable("authorName", "We Independent"); // 显示作者名
        context.setVariable("createTime", currentTime);
        context.setVariable("content", blog.getContent());

        // 3. 渲染 HTML 字符串
        String html = templateEngine.process("blog_pdf_template", context);
        // 3.1 清洗 HTML 中的非法 & 符号，避免 PDF 渲染异常
        String safeHtml = HtmlSanitizerUtil.cleanHtmlForXml(html);
        
        // 4. 用 OpenHTMLtoPDF 渲染为 PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            String fontPath = this.getClass().getClassLoader().getResource("fonts/NotoSansSC-Regular.ttf").getPath();
            builder.useFastMode();  // 更换模式
            builder.withHtmlContent(safeHtml, null);
            builder.useFont(
            new File(fontPath),
            "Noto Sans SC",
            400,
            BaseRendererBuilder.FontStyle.NORMAL,
            true
            );
            builder.toStream(outputStream);
            builder.run(); // PDF 生成
            return outputStream.toByteArray(); // PDF 内容通过 byte[]返回

        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Fail to create PDF" + e.getMessage(), e);
        }
    
    }

}
