package com.weindependent.app.service.impl;

import com.weindependent.app.database.dataobject.BlogPdfDownloadLogDO;
import com.weindependent.app.database.mapper.weindependent.BlogPdfDownloadLogMapper;
import com.weindependent.app.service.IBlogPdfDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("downloadStatisticsService")
public class BlogPdfDownloadServiceImpl implements IBlogPdfDownloadService {

    @Autowired
    private BlogPdfDownloadLogMapper blogDownloadLogMapper;

    @Override
    public void updateStatistics(Long blogId, Long userId, LocalDateTime downloadTime) {
        BlogPdfDownloadLogDO log = new BlogPdfDownloadLogDO();
        log.setBlogId(blogId.intValue());
        log.setUserId(userId);
        log.setDownloadTime(downloadTime);
        blogDownloadLogMapper.insertLog(log);
    }

    @Override
    public int getDownloadCount(Long blogId) {
        return blogDownloadLogMapper.getDownloadCount(blogId);
    }
}
