package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.mapper.dashboard.BlogPdfMapper;
import com.weindependent.app.database.dataobject.BlogPdfDO;
import com.weindependent.app.utils.PageInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogPdfService;

/**
 * 博客文章pdfService业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *    2025-03-23
 */
@Service
public class BlogPdfServiceImpl implements IBlogPdfService 
{
    @Autowired
    private BlogPdfMapper blogPdfMapper;

    /**
     * 查询博客文章pdf
     * 
     * @param id 博客文章pdf主键
     * @return 博客文章pdf
     */
    @Override
    public BlogPdfDO selectBlogPdfById(Integer id)
    {
        return blogPdfMapper.selectBlogPdfById(id);
    }

    /**
     * 查询博客文章pdf列表
     * 
     * @param blogPdf 博客文章pdf
     * @return 博客文章pdf
     */
    @Override
    public PageInfo<BlogPdfDO> selectBlogPdfList(BlogPdfDO blogPdf, int pageNum, int pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<BlogPdfDO> BlogPdfDOList =  blogPdfMapper.selectBlogPdfList(blogPdf);
        PageInfo<BlogPdfDO> BlogPdfDOPageInfo = new PageInfo<>(BlogPdfDOList);
        return PageInfoUtil.pageInfo2DTO(BlogPdfDOPageInfo, BlogPdfDO.class);

    }

    /**
     * 新增博客文章pdf
     * 
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    @Override
    public int insertBlogPdf(BlogPdfDO blogPdf)
    {
        blogPdf.setCreateTime(LocalDateTime.now());
        return blogPdfMapper.insertBlogPdf(blogPdf);
    }

    /**
     * 修改博客文章pdf
     * 
     * @param blogPdf 博客文章pdf
     * @return 结果
     */
    @Override
    public int updateBlogPdf(BlogPdfDO blogPdf)
    {
        blogPdf.setUpdateTime(LocalDateTime.now());
        return blogPdfMapper.updateBlogPdf(blogPdf);
    }

    /**
     * 批量删除博客文章pdf
     * 
     * @param ids 需要删除的博客文章pdf主键
     * @return 结果
     */
    @Override
    public int deleteBlogPdfByIds(Integer[] ids)
    {
        return blogPdfMapper.deleteBlogPdfByIds(ids);
    }

    /**
     * 删除博客文章pdf信息
     * 
     * @param id 博客文章pdf主键
     * @return 结果
     */
    @Override
    public int deleteBlogPdfById(Integer id)
    {
        return blogPdfMapper.deleteBlogPdfById(id);
    }
}
