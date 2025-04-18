package com.weindependent.app.database.mapper.weindependent;
import com.weindependent.app.database.dataobject.BlogPdfStorageDO;

import org.apache.ibatis.annotations.Param;

public interface BlogPdfStorageMapper {
    /**
     * 根据博客ID查询PDF存储记录
     * @param blogId 博客ID
     * @return BlogPdfStorageDO 对象或null
     */
    BlogPdfStorageDO selectPdfByBlogId(@Param("blogId") Integer blogId);

    /**
     * 插入一条PDF存储记录
     * @param storage BlogPdfStorageDO 对象
     * @return 受影响的行数
     */
    int insertPdf(BlogPdfStorageDO storage);
}
