package com.weindependent.app.service.impl;

import com.weindependent.app.database.dataobject.BlogPdfStorageDO;
import com.weindependent.app.database.mapper.weindependent.BlogPdfStorageMapper;
import com.weindependent.app.service.IBlogPdfStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service("blogPdfStorageService")
public class BlogPdfStorageServiceImpl implements IBlogPdfStorageService {

    @Autowired
    private BlogPdfStorageMapper blogPdfStorageMapper;

    @Override
    public BlogPdfStorageDO getPdfByBlogId(Integer blogId) {
        return blogPdfStorageMapper.selectPdfByBlogId(blogId);
    }

    @Override
    public void savePdf(Integer blogId, byte[] pdfContent) {
        BlogPdfStorageDO storage = new BlogPdfStorageDO();
        storage.setBlogId(blogId);
        storage.setPdfContent(pdfContent);
        // 这里fileName定义在controller里
        storage.setFileName("WeIndependent_blog_" + blogId + ".pdf");
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        storage.setPdfGenerationTime(date);
        // 逻辑删除标记：0表示未删除
        storage.setIsDeleted(0);
        storage.setCreateUserId(null);
        storage.setCreateTime(LocalDateTime.now());
        storage.setUpdateUserId(null);
        storage.setUpdateTime(LocalDateTime.now());
        blogPdfStorageMapper.insertPdf(storage);
    }
}
