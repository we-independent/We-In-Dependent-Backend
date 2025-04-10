package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
import com.weindependent.app.vo.BlogArticleVO;
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
        blogImageDO.setCategory("banner");
        blogImageDO.setFileName(uploadedFileVO.getFileName());
        blogImageDO.setFileType(uploadedFileVO.getFileType());
        blogImageDO.setFilePath(uploadedFileVO.getFilePath());
        int affectedRows = blogImageMapper.insert(blogImageDO);
        if (affectedRows != 1) {
            throw new RuntimeException("Failed to add image to database");
        }
        uploadedFileVO.setFileId(blogImageDO.getId());
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
        BlogArticleDO oldBlog=blogArticleMapper.selectBlogArticleById(blogArticle.getId());
        if(!oldBlog.getBannerImgId().equals(blogArticle.getBannerImgId())){
            deleteImgById(oldBlog.getBannerImgId());
        }
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
        for(Integer id:ids){
            BlogArticleDO blog=blogArticleMapper.selectBlogArticleById(id);
            deleteImgById(blog.getBannerImgId());
        }
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
        BlogArticleDO blog=blogArticleMapper.selectBlogArticleById(id);
        deleteImgById(blog.getBannerImgId());
        return blogArticleMapper.deleteBlogArticleById(id);
    }

    /**
     * 全文搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    @Override
    public List<BlogArticleVO> searchByContent(String keyword){
        List<BlogArticleDO> blogArticleDOS = blogArticleMapper.searchByContent(keyword);

        return blogArticleDOS.stream().map(this::toBlogVO).collect(Collectors.toList());
    }

    /**
     * 关键词搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    @Override
    public List<BlogArticleVO> searchByExactKeywords(String keyword){
        List<BlogArticleDO> blogArticleDOS = blogArticleMapper.searchByExactKeywords(keyword);

        return blogArticleDOS.stream().map(this::toBlogVO).collect(Collectors.toList());
    }

    private BlogArticleVO toBlogVO(BlogArticleDO blogDO) {
        if (blogDO == null) return null;

        BlogArticleVO blogVO = new BlogArticleVO();
        blogVO.setId(blogDO.getId());
        blogVO.setArticleSourceType(blogDO.getArticleSourceType());
        blogVO.setSourceUrl(blogDO.getSourceUrl());
        blogVO.setAuthorId(blogDO.getAuthorId());
        blogVO.setBannerImgId(blogDO.getBannerImgId());
        blogVO.setSummary(blogDO.getSummary());
        blogVO.setTitle(blogDO.getTitle());
        blogVO.setContent(blogDO.getContent());
        blogVO.setArticleStatus(blogDO.getArticleStatus());
        blogVO.setCategoryId(blogDO.getCategoryId());
        blogVO.setIsDeleted(blogDO.getIsDeleted());
        blogVO.setCreateUserId(blogDO.getCreateUserId());
        blogVO.setCreateTime(blogDO.getCreateTime());
        blogVO.setUpdateUserId(blogDO.getUpdateUserId());
        blogVO.setUpdateTime(blogDO.getUpdateTime());
        return blogVO;
    }

    private void deleteImgById(Integer imgId){
        BlogImageDO image= blogImageMapper.findById(imgId);
        image.setIsDeleted(1);
        blogImageMapper.update(image);
        fileService.deleteFile(image.getFilePath());
    }
}
