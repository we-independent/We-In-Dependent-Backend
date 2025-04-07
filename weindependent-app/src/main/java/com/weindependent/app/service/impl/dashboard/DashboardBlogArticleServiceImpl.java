package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogImageDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogArticleMapper;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogImageMapper;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.dto.FileUploadQry;
import com.weindependent.app.service.FileService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.vo.UploadedFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogArticleService;

import javax.annotation.Resource;


/**
 * 博客文章Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 * 
 * @author christina
 *  2025-03-23
 */
@Service
public class DashboardBlogArticleServiceImpl implements IBlogArticleService
{
    private final DashboardBlogArticleMapper blogArticleMapper;
    @Resource
    private DashboardBlogImageMapper blogImageMapper;

    @Resource
    private FileService fileService;

    public DashboardBlogArticleServiceImpl(DashboardBlogArticleMapper dashboardBlogArticleMapper) {
        this.blogArticleMapper = dashboardBlogArticleMapper;
    }


    /**
     * 查询博客文章
     * 
     * @param id 博客文章主键
     * @return 博客文章
     */
    @Override
    public BlogArticleDO selectBlogArticleById(Integer id)
    {
        return blogArticleMapper.selectBlogArticleById(id);
    }

    /**
     * 查询博客文章列表
     * 
     * @param blogArticleQry 博客文章
     * @return 博客文章
     */
    @Override
    public PageInfo<BlogArticleDO> selectBlogArticleList(BlogArticleQry blogArticleQry)
    {
        BlogArticleDO blogArticleDO = new BlogArticleDO();
        BeanUtils.copyProperties(blogArticleDO,blogArticleDO);
        PageHelper.startPage(blogArticleQry.getPageNum(), blogArticleQry.getPageSize());
        List<BlogArticleDO> BlogArticleDOList = blogArticleMapper.selectBlogArticleList(blogArticleDO);
        PageInfo<BlogArticleDO> PageInfo = new PageInfo<>(BlogArticleDOList);
        return PageInfoUtil.pageInfo2DTO(PageInfo, BlogArticleDO.class);
    }

    /**
     * 新增博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    public int insertBlogArticle(BlogArticleDO blogArticle)
    {
        blogArticle.setCreateTime(LocalDateTime.now());
        return blogArticleMapper.insertBlogArticle(blogArticle);
    }

    @Override
    public UploadedFileVO insertBlogBanner(FileUploadQry fileUploadQry)
    {
        UploadedFileVO uploadedFileVO = fileService.uploadFile(fileUploadQry.getFile(), fileUploadQry.getCategory());
        BlogImageDO blogImageDO = new BlogImageDO();
        blogImageDO.setCategory(fileUploadQry.getCategory());
        blogImageDO.setFileName(uploadedFileVO.getFileName());
        blogImageDO.setFileType(uploadedFileVO.getFileType());
        blogImageDO.setFilePath(uploadedFileVO.getFilePath());
        int blogImageId = blogImageMapper.insert(blogImageDO);
        uploadedFileVO.setFileId(blogImageId);
        return uploadedFileVO;
    }

    /**
     * 修改博客文章
     * 
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    public int updateBlogArticle(BlogArticleDO blogArticle)
    {
        blogArticle.setUpdateTime(LocalDateTime.now());
        return blogArticleMapper.updateBlogArticle(blogArticle);
    }

    /**
     * 批量删除博客文章
     * 
     * @param ids 需要删除的博客文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleByIds(Integer[] ids)
    {
        return blogArticleMapper.deleteBlogArticleByIds(ids);
    }

    /**
     * 删除博客文章信息
     * 
     * @param id 博客文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleById(Integer id)
    {
        return blogArticleMapper.deleteBlogArticleById(id);
    }
}
