package com.weindependent.app.database.mapper.weindependent;

import org.apache.ibatis.annotations.Param;

import com.weindependent.app.database.dataobject.BlogPdfDownloadLogDO;

public interface BlogPdfDownloadLogMapper {

    /**
     * 插入下载日志记录
     */
    int insertLog(BlogPdfDownloadLogDO log);
    /**
     * 查询指定博客的下载次数
     * @param blogId 博客ID
     * @return 下载次数
     */
    int getDownloadCount(@Param("blogId") Long blogId);
}
