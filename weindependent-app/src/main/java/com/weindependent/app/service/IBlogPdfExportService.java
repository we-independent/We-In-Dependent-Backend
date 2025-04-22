package com.weindependent.app.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.dto.BlogPdfQry;

/**
 * 博客文章pdfService接口
 * 
 * @author Hurely
 *    2025-04-2
 */
public interface IBlogPdfExportService 
{
    /**
    /**
     * 根据博客 ID 生成 PDF 文件内容（字节流形式）
     *
     * @param id 博客文章的主键 ID
     * @return 生成的 PDF 文件内容（byte 数组）
     * 
     */
    public byte[] generatePdf(Integer id);
}
