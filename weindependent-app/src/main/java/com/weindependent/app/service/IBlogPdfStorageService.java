package com.weindependent.app.service;

import com.weindependent.app.database.dataobject.BlogPdfStorageDO;

public interface IBlogPdfStorageService {
    /**
     * 根据博客ID获取PDF存储记录
     * @param blogId 博客ID
     * @return BlogPdfStorageDO 对象，如果不存在则返回null
     */
    BlogPdfStorageDO getPdfByBlogId(Integer blogId);

    /**
     * 保存PDF数据到数据库
     * @param blogId 博客ID
     * @param pdfContent PDF文件的二进制数据
     */
    void savePdf(Integer blogId, byte[] pdfContent);
}
