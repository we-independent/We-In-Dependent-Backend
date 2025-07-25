package com.weindependent.app.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayOutputStream;
import java.io.File;

import com.weindependent.app.database.mapper.weindependent.BlogPdfExportMapper;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.utils.HtmlSanitizerUtil;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;

import com.weindependent.app.service.IBlogPdfExportService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service("blogPdfExportServiceImpl")
public class BlogPdfExportServiceImpl implements IBlogPdfExportService  {
    @Autowired
    private BlogPdfExportMapper blogpdfExpMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public byte[] generatePdf(Integer id) {
        BlogArticleDO blog = blogpdfExpMapper.selectBlogPdfExpById(id);
        if (blog == null) {
            throw new RuntimeException("Blog not found. ID=" + id);
        }

        Context context = new Context();
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        context.setVariable("title", blog.getTitle());
        context.setVariable("summary", blog.getSummary());
        context.setVariable("authorName", "We Independent");
        context.setVariable("createTime", currentTime);
        context.setVariable("content", blog.getContent());

        String html = templateEngine.process("blog_pdf_template", context);
        // 替换非法 HTML 实体字符，避免 PDF 生成失败
        html = html
                .replace("&nbsp;", " ")
                .replace("&hellip;", "...")
                .replace("&copy;", "(c)")
                .replaceAll("<br(?!\\s*/)>", "<br />")
                .replaceAll("<hr(?!\\s*/)>", "<hr />")
                .replaceAll("<img([^>]*)[^/]>", "<img$1 />");
                String safeHtml = HtmlSanitizerUtil.cleanHtmlForXml(html);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            try (InputStream fontStream = this.getClass().getClassLoader().getResourceAsStream("fonts/NotoSansSC-Regular.ttf")) {
                System.out.println("字体流是否获取成功：" + (fontStream != null));
                if (fontStream != null) {
                    builder.useFont(() -> fontStream, "Noto Sans SC", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
                } else {
                    throw new ResponseException(ErrorCode.FONT_NOT_EXIST.getCode(), "字体不存在");
                }
            }
            // String fontPath = this.getClass().getClassLoader().getResource("fonts/NotoSansSC-Regular.ttf").getPath();
            builder.useFastMode();
            builder.withHtmlContent(safeHtml, null);
            // builder.useFont(new File(fontStream), "Noto Sans SC", 400, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Fail to create PDF: " + e.getMessage(), e);
        }
    }

}
