package com.weindependent.app.database.mapper.weindependent;

import com.weindependent.app.database.dataobject.BlogArticlePdfListDO;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.dto.BlogArticlePdfListQry;
import com.weindependent.app.dto.BlogPdfQry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogPdfListMapper {

    /**
     * 分页条件查询 PDF 列表
     */
    List<BlogArticlePdfListDO> selectBlogPdfList(@Param("query") BlogArticlePdfListQry query);

    /**
     * 插入 PDF
     */
    int insertBlogPdf(BlogPdfDO pdfDO);

    /**
     * 更新 PDF
     */
    int updateBlogPdf(BlogPdfDO pdfDO);

    /**
     * 删除单个 PDF
     */
    int deleteBlogPdfById(Integer id);

    /**
     * 批量删除 PDF
     */
    int deleteBlogPdfByIds(@Param("ids") Integer[] ids);
}
