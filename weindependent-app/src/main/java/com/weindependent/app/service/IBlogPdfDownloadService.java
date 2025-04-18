package com.weindependent.app.service;

import java.time.LocalDateTime;


/**
 * 博客文章pdfdownload数据Service接口
 * 
 * @author Hurely
 *    2025-04-13
 */

public interface IBlogPdfDownloadService {
    /**
     * 更新下载统计信息，记录博客 ID、用户 ID 以及下载时间
     *
     * @param blogId 博客ID
     * @param userId 下载该博客的用户ID
     * @param downloadTime 下载时间
     * 
     */
    void updateStatistics(Integer blogId, Long userId, LocalDateTime downloadTime);
    /**
     * 获取指定博客的下载次数
     * 
     * @param blogId 博客ID
     * @return 下载次数
     */
    int getDownloadCount(Integer blogId);
}
