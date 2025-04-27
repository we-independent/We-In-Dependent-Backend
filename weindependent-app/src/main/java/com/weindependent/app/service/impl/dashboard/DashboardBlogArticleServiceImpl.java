package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.weindependent.app.convertor.BlogConverter;
import com.weindependent.app.database.dataobject.TagArticleRelationDO;
import com.weindependent.app.database.dataobject.TagDO;
import com.weindependent.app.database.mapper.dashboard.DashboardTagArticleRelationMapper;
import com.weindependent.app.database.mapper.dashboard.DashboardTagMapper;
import com.weindependent.app.dto.BlogArticleAddQry;
import lombok.extern.slf4j.Slf4j;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.BlogImageDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogArticleMapper;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogImageMapper;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.service.FileService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.vo.BlogArticleVO;
import com.weindependent.app.vo.UploadedFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weindependent.app.service.IBlogArticleService;

import javax.annotation.Resource;


import com.weindependent.app.utils.ImageResizeUtil;
import org.springframework.web.multipart.MultipartFile;

/**
 * 博客文章Service业务层处理
 * dashboard 查询包含 isDeleted=1 的 record
 *
 * @author christina
 *  2025-03-23
 */
@Service
@Slf4j
public class DashboardBlogArticleServiceImpl implements IBlogArticleService
{
    private final DashboardBlogArticleMapper blogArticleMapper;
    @Resource
    private DashboardBlogImageMapper blogImageMapper;
    @Resource
    private FileService fileService;
    @Autowired
    private DashboardTagArticleRelationMapper dashboardTagArticleRelationMapper;
    @Autowired
    private DashboardTagMapper dashboardTagMapper;

    private final Integer RESIZE_WIDTH = 1729;
    private final Integer RESIZE_HEIGHT = 438;

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
    public BlogArticleDO selectBlogArticleById(Integer id) {
        return blogArticleMapper.selectBlogArticleById(id);
    }

    /**
     * 查询博客文章列表
     *
     * @param blogArticleQry 博客文章
     * @return 博客文章
     */
    @Override
    public PageInfo<BlogArticleDO> selectBlogArticleList(BlogArticleQry blogArticleQry) {
        BlogArticleDO blogArticleDO = new BlogArticleDO();
        BeanUtils.copyProperties(blogArticleQry, blogArticleDO);
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
    public int insertBlogArticle(BlogArticleAddQry blogArticle, Integer userId) {
        //1. 新增blogArticle信息
        BlogArticleDO blogArticleDO = new BlogArticleDO();
        BeanUtils.copyProperties(blogArticle, blogArticleDO);
        blogArticleDO.setCreateUserId(userId);
        blogArticleDO.setUpdateUserId(userId);
        blogArticleDO.setCreateTime(LocalDateTime.now());

        if (blogArticleMapper.insertBlogArticle(blogArticleDO) != 1) {
            throw new RuntimeException("Failed to add Article to database");
        }
        int articleId = blogArticleDO.getId();

        // 2 根据tags 新增tag信息:
        // 2.1 不存在相同tagname，新增
        // 2.2 存在相同tagname， 如果已经删除，恢复
        //allTagList，用于创建关系数据
        List<TagDO> allTagList = new ArrayList<>();
        for (String s : blogArticle.getTags()) {
            TagDO tag = dashboardTagMapper.selectTagByName(s);
            if (tag == null) {
                tag = new TagDO();
                tag.setName(s);
                tag.setCreateUserId(userId);
                tag.setUpdateUserId(userId);
                tag.setCreateTime(LocalDateTime.now());
                if (dashboardTagMapper.insertTag(tag) != 1) {
                    throw new RuntimeException("Failed to add tag to database, tagName: " + s);
                }
            } else if (tag.getIsDeleted()) {
                tag.setUpdateUserId(userId);
                if (dashboardTagMapper.recoverTag(tag) != 1) {
                    throw new RuntimeException("Failed to recover tag in database, tagName: " + s);
                }
            }
            allTagList.add(tag);
        }
        // 3 根据allTagList添加 tag article relation：
        for (TagDO tag : allTagList) {
            TagArticleRelationDO relation = new TagArticleRelationDO();
            relation.setArticleId(articleId);
            relation.setTagId(tag.getId());
            relation.setCreateUserId(userId);
            relation.setCreateTime(LocalDateTime.now());
            relation.setUpdateUserId(userId);
            if (dashboardTagArticleRelationMapper.insertTagArticleRelation(relation) != 1) {
                throw new RuntimeException("Failed to add tag to database, tagName: " + tag.getName());
            }
        }

        return 1;
    }

    @Override
    public BlogImageDO insertBlogBanner(MultipartFile file) {
        // Resize image first
        MultipartFile resizedFile;
        try {
            resizedFile = ImageResizeUtil.resizeImage(file, RESIZE_WIDTH, RESIZE_HEIGHT);
        } catch (Exception e) {
            log.error("Failed to resize image before uploading: {}", e.getMessage());
            throw new RuntimeException("Failed to resize image");
        }

        // Then upload
        UploadedFileVO uploadedFileVO = fileService.uploadFile(resizedFile, null,"blog-banner" );

        BlogImageDO blogImageDO = new BlogImageDO();
        blogImageDO.setCategory("blog-banner");
        blogImageDO.setFileName(uploadedFileVO.getFileName());
        blogImageDO.setFileKey(uploadedFileVO.getFileKey());
        blogImageDO.setFileType(resizedFile.getContentType());
        blogImageDO.setFilePath(uploadedFileVO.getFilePath());
        int affectedRows = blogImageMapper.insert(blogImageDO);
        if (affectedRows != 1) {
            throw new RuntimeException("Failed to add image to database");
        }
        return blogImageDO;
    }

    /**
     * 修改博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
    @Override
    public int updateBlogArticle(BlogArticleAddQry blogArticle, Integer userId) {

        //Hurely add null exception
        if (blogArticle.getId() == null) {
            throw new IllegalArgumentException("Blog ID can not be null!");
        }

        BlogArticleDO oldBlog=blogArticleMapper.selectBlogArticleById(blogArticle.getId());
        if(!oldBlog.getBannerImgId().equals(blogArticle.getBannerImgId())){
            deleteImgById(oldBlog.getBannerImgId());
        }
        BlogArticleDO blogArticleDO = new BlogArticleDO();
        BeanUtils.copyProperties(blogArticle, blogArticleDO);
        blogArticleDO.setUpdateUserId(userId);
        blogArticleDO.setUpdateTime(LocalDateTime.now());
        blogArticleMapper.updateBlogArticle(blogArticleDO );

        //1. 修改blogArticle信息
        if (blogArticleMapper.insertBlogArticle(blogArticleDO) != 1) {
            throw new RuntimeException("Failed to add Article to database");
        }

        // 2 根据tags 新增tag信息:
        // 2.1 不存在相同tagname，新增
        // 2.2 存在相同tagname， 如果已经删除，恢复
        //allTagList，用于创建关系数据
        List<TagDO> allTagList = new ArrayList<>();
        for (String s : blogArticle.getTags()) {
            TagDO tag = dashboardTagMapper.selectTagByName(s);
            if (tag == null) {
                tag = new TagDO();
                tag.setName(s);
                tag.setCreateUserId(userId);
                tag.setUpdateUserId(userId);
                tag.setCreateTime(LocalDateTime.now());
                if (dashboardTagMapper.insertTag(tag) != 1) {
                    throw new RuntimeException("Failed to add tag to database, tagName: " + s);
                }
            } else if (tag.getIsDeleted()) {
                tag.setUpdateUserId(userId);
                if (dashboardTagMapper.recoverTag(tag) != 1) {
                    throw new RuntimeException("Failed to recover tag in database, tagName: " + s);
                }
            }
            allTagList.add(tag);
        }
        // 3. 根据ArticleId删除所有 tag article relation
        // 4. 根据allTagList重新添加 tag article relation：
        // 4.1. tag article relation不存在，新增
        // 4.2. 如果tag article relation已经删除，恢复并且修改updatetime
        dashboardTagArticleRelationMapper.deleteByArticleId(blogArticle.getId(),userId);
        for (TagDO tag : allTagList) {
            TagArticleRelationDO relation = dashboardTagArticleRelationMapper.getRelationByTagIdAndArticleId(tag.getId(), blogArticle.getId());
            if (relation == null) {
                relation = new TagArticleRelationDO();
                relation.setArticleId(blogArticle.getId());
                relation.setTagId(tag.getId());
                relation.setCreateUserId(userId);
                relation.setCreateTime(LocalDateTime.now());
                relation.setUpdateUserId(userId);
                if (dashboardTagArticleRelationMapper.insertTagArticleRelation(relation) != 1) {
                    throw new RuntimeException("Failed to add tag to database, tagName: " + tag.getName());
                }
            } else if (relation.getIsDeleted()) {
                if (dashboardTagArticleRelationMapper.recoverById(relation.getId(),userId) != 1)
                    throw new RuntimeException("Failed to recover tag article relation in database, tagName: " + tag.getName());
            }
        }
        return 1;
    }

    /**
     * 批量删除博客文章
     *
     * @param ids 需要删除的博客文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleByIds(Integer[] ids, int updateUserId) {
        for (Integer id : ids) {
            BlogArticleDO blog = blogArticleMapper.selectBlogArticleById(id);
            deleteImgById(blog.getBannerImgId());
            dashboardTagArticleRelationMapper.deleteByArticleId(id, updateUserId);
        }

        return blogArticleMapper.deleteBlogArticleByIds(ids, updateUserId);
    }

    /**
     * 删除博客文章信息
     *
     * @param id 博客文章主键
     * @return 结果
     */
    @Override
    public int deleteBlogArticleById(Integer id) {
        BlogArticleDO blog = blogArticleMapper.selectBlogArticleById(id);
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
    public List<BlogArticleVO> searchByContent(String keyword) {
        List<BlogArticleDO> blogArticleDOS = blogArticleMapper.searchByContent(keyword);

        return blogArticleDOS.stream().map(BlogConverter::toBlogVO).collect(Collectors.toList());
    }

    /**
     * 关键词搜索
     *
     * @param keyword 关键词
     * @return 结果
     */
    @Override
    public List<BlogArticleVO> searchByExactKeywords(String keyword) {
        List<BlogArticleDO> blogArticleDOS = blogArticleMapper.searchByExactKeywords(keyword);

        return blogArticleDOS.stream().map(BlogConverter::toBlogVO).collect(Collectors.toList());
    }



    private void deleteImgById(Integer imgId){
        //Hurely add null exception handling
        if (imgId == null) {
            log.warn("deleteImgById 调用时传入的 imgId 为 null, 跳过删除");
            return;
        }
        BlogImageDO image= blogImageMapper.findById(imgId);
        if (image == null) {
            log.warn("未找到图片,imgId = {}，跳过删除", imgId);
            return;
        }

        image.setIsDeleted(1);
        blogImageMapper.update(image);

        if (image.getFilePath() != null) {
            fileService.deleteFile(image.getFileKey());
        }
        else {
            log.warn("图片文件路径为空,无法执行文件删除操作,imageId={}", imgId);
        }

        log.info("成功逻辑删除图片：{}", imgId);

    }
}
