package com.weindependent.app.service;

import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.dataobject.BlogArticlePdfListDO;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.dto.BlogArticlePdfListQry;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.dto.BlogPdfQry;

/**
 * 博客PDF列表 Service接口
 * 
 * @author 
 * @date 2025-04-05
 */
public interface IBlogPdfListService {

    /**
     * 条件分页查询 PDF 文件列表
     * 
     * @param query 查询参数
     * @return 分页结果
     */

    PageInfo<BlogArticlePdfListDO> selectBlogPdfList(BlogArticlePdfListQry blogArticlePdfListQry);
}
