package com.weindependent.app.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogArticlePdfListDO;
import com.weindependent.app.database.mapper.weindependent.BlogPdfListMapper;
import com.weindependent.app.dto.BlogArticlePdfListQry;
import com.weindependent.app.service.IBlogPdfListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPdfListServiceImpl implements IBlogPdfListService {

    @Autowired
    private BlogPdfListMapper blogPdfListMapper;

    @Override
    public PageInfo<BlogArticlePdfListDO> selectBlogPdfList(BlogArticlePdfListQry query) {
        int pageNum = query.getPageNum() == null || query.getPageNum() <= 0 ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null || query.getPageSize() <= 0 ? 10 : query.getPageSize();

        PageHelper.startPage(pageNum, pageSize);

        List<BlogArticlePdfListDO> list = blogPdfListMapper.selectBlogPdfList(query);
        return new PageInfo<>(list);
    }

}
