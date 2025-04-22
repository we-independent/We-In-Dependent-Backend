package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.database.dataobject.BlogPdfStorageDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 博客文章pdfMapper接口
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author 
 */
@Mapper
public interface BlogPdfExportMapper 
{
    /**
     * 查询博客文章pdf
     * 
     * @param id 博客文章pdf主键
     * @return 博客文章pdf
     */
    public BlogArticleDO selectBlogPdfExpById(Integer id);

    public BlogPdfDO selectByArticleIdIgnoreDeleted(Integer id);
    int insertPdf(BlogPdfDO blogPdf);
    int updateById(BlogPdfDO blogPdf);

    public BlogPdfStorageDO selectStorageByBlogId(Integer blogId);

    public void updateStorageByBlogId(BlogPdfStorageDO storage);

    public void insertStorage(BlogPdfStorageDO newStorage);

}
