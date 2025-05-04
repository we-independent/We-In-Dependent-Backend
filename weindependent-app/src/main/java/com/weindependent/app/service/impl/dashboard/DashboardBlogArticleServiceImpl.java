package com.weindependent.app.service.impl.dashboard;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.weindependent.app.convertor.BlogConverter;
import com.weindependent.app.database.dataobject.TagArticleRelationDO;
import com.weindependent.app.database.mapper.dashboard.DashboardTagArticleRelationMapper;
import com.weindependent.app.database.mapper.dashboard.DashboardTagMapper;
import com.weindependent.app.dto.BlogArticleEditQry;
import com.weindependent.app.utils.CommonUtil;
import com.weindependent.app.vo.BlogArticleEditVO;
import lombok.extern.slf4j.Slf4j;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.weindependent.app.database.dataobject.ImageDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogArticleMapper;
import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.database.mapper.dashboard.DashboardBlogImageMapper;
import com.weindependent.app.dto.BlogArticleQry;
import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.ResponseException;
import com.weindependent.app.service.FileService;
import com.weindependent.app.utils.PageInfoUtil;
import com.weindependent.app.vo.BlogArticleVO;
import com.weindependent.app.vo.UploadedFileVO;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;

import io.opencensus.tags.Tag;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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
     * 为博客编辑页面查询博客文章
     *
     * @param id 博客文章主键
     * @return 博客文章
     */
    @Override
    public BlogArticleEditVO selectBlogArticleByIdForEdit(Integer id){
        BlogArticleEditVO blogArticleEditVO =  blogArticleMapper.selectBlogArticleEditVOById(id);
        blogArticleEditVO.setBannerImgUrl(CommonUtil.convertToImgSrc(blogArticleEditVO.getBannerImgUrl(),200));
        List<Integer> tagIdList = dashboardTagArticleRelationMapper.getTagIdListByArticleId(id);
        blogArticleEditVO.setTagIdList(tagIdList);
        return blogArticleEditVO;
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
    public int insertBlogArticle(BlogArticleEditQry blogArticle, Integer userId) {
        //1. 新增blogArticle信息
        BlogArticleDO blogArticleDO = new BlogArticleDO();
        BeanUtils.copyProperties(blogArticle, blogArticleDO);
        blogArticleDO.setCreateUserId(userId);
        blogArticleDO.setUpdateUserId(userId);
        blogArticleDO.setCreateTime(LocalDateTime.now());

        if (blogArticleMapper.insertBlogArticle(blogArticleDO) != 1) {
            throw new RuntimeException("Failed to add Article to database");
        }

        //2. 新增 tag article relation
        for ( Integer tagId : blogArticle.getTags()) {
            TagArticleRelationDO relation = new TagArticleRelationDO();
            relation.setArticleId(blogArticleDO.getId());
            relation.setTagId(tagId);
            relation.setCreateUserId(userId);
            relation.setCreateTime(LocalDateTime.now());
            relation.setUpdateUserId(userId);

            if (dashboardTagArticleRelationMapper.insertTagArticleRelation(relation) != 1) {
                throw new RuntimeException("Failed to add TagArticleRelation to database, tagName: " + tagId + ", articleId: " + blogArticleDO.getId());
            }
        }

        return 1;
    }

    @Override
    public ImageDO insertBlogBanner(MultipartFile file) {
        // Resize image first
        MultipartFile resizedFile;
        try {
            resizedFile = ImageResizeUtil.resizeImage(file, RESIZE_WIDTH, RESIZE_HEIGHT);
        } catch (Exception e) {
            log.error("Failed to resize image before uploading: {}", e.getMessage());
            throw new RuntimeException("Failed to resize image");
        }

        // Then upload
        UploadedFileVO uploadedFileVO = fileService.uploadFile(resizedFile, null,"event-banner" );

        ImageDO imageDo = new ImageDO();
        imageDo.setCategory("blog-banner");
        imageDo.setFileName(uploadedFileVO.getFileName());
        imageDo.setFileKey(uploadedFileVO.getFileKey());
        imageDo.setFileType(resizedFile.getContentType());
        imageDo.setFilePath(uploadedFileVO.getFilePath());
        int affectedRows = blogImageMapper.insert(imageDo);
        if (affectedRows != 1) {
            throw new RuntimeException("Failed to add image to database");
        }
        return imageDo;
    }

    /**
     * 修改博客文章
     *
     * @param blogArticle 博客文章
     * @return 结果
     */
    // @Override
    // public int updateBlogArticle(BlogArticleEditQry blogArticle, Integer userId) {

        // //Hurely add null exception
        // if (blogArticle.getId() == null) {
        //     throw new IllegalArgumentException("Blog ID can not be null!");
        // }

        // BlogArticleDO oldBlog=blogArticleMapper.selectBlogArticleById(blogArticle.getId());
        // if(!oldBlog.getBannerImgId().equals(blogArticle.getBannerImgId())){
        //     deleteImgById(oldBlog.getBannerImgId());
        // }

        // //1. 修改blogArticle信息
        // BlogArticleDO blogArticleDO = new BlogArticleDO();
        // BeanUtils.copyProperties(blogArticle, blogArticleDO);
        // blogArticleDO.setUpdateUserId(userId);
        // blogArticleDO.setUpdateTime(LocalDateTime.now());

        // if (blogArticleMapper.updateBlogArticle(blogArticleDO ) != 1) {
        //     throw new RuntimeException("Failed to update Article to database");
        // }
    
    //     //2. 删除现有TagArticleRelation
    //     dashboardTagArticleRelationMapper.deleteByArticleId(blogArticle.getId(), userId);

    //     //3. 新增 tag article relation
    //     for (Integer tagId : blogArticle.getTags()) {
    //         TagArticleRelationDO relation = dashboardTagArticleRelationMapper.getRelationByTagIdAndArticleId(tagId, blogArticle.getId());
    //         if (relation == null) {
    //             relation = new TagArticleRelationDO();
    //             relation.setArticleId(blogArticle.getId());
    //             relation.setTagId(tagId);
    //             relation.setCreateUserId(userId);
    //             relation.setCreateTime(LocalDateTime.now());
    //             relation.setUpdateUserId(userId);
    //             relation.setUpdateTime(LocalDateTime.now());
    //             if (dashboardTagArticleRelationMapper.insertTagArticleRelation(relation) != 1) {
    //                 throw new RuntimeException("Failed to add  tag article relation to database, tagId: " + tagId  + ", articleId: " + blogArticle.getId());
    //             }
    //         } else if (relation.getIsDeleted()) {
    //             if (dashboardTagArticleRelationMapper.recoverById(relation.getId(),userId) != 1)
    //                 throw new RuntimeException("Failed to recover tag article relation in database, tagId: " + tagId + ", articleId: " + blogArticle.getId());
    //         }
    //     }
    //     return 1;
    // }

    @Override
    public int updateBlogArticle(BlogArticleEditQry blogArticle, Integer userId) {
        System.out.println("🟠 前端传入 tags = " + blogArticle.getTags());

        if (blogArticle.getId() == null) {
            throw new IllegalArgumentException("Blog ID can not be null!");
        }
        // 1. 修改blogArticle信息
        BlogArticleDO blogArticleDO = new BlogArticleDO();
        BeanUtils.copyProperties(blogArticle, blogArticleDO);
        blogArticleDO.setUpdateUserId(userId);
        blogArticleDO.setUpdateTime(LocalDateTime.now());
    
        // 2. 更新文章和 banner（旧 banner 自动逻辑软删除）
        int result = blogArticleMapper.updateBlogArticleWithBanner(blogArticleDO);
        if (result == 0) {
            throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to update article in database:"+ blogArticle.getId());
        }
        // 3. 删除不再使用的标签，保留依旧使用的标签
        List<Integer> tagList = blogArticle.getTags();
        dashboardTagArticleRelationMapper.deleteByArticleIdExcludeTags(blogArticle.getId(), tagList, userId);
        
        // 4. 构造需要 upsert 的标签关系对象
        List<TagArticleRelationDO> tagRelations = blogArticle.getTags().stream().map(tagId -> {
            TagArticleRelationDO relation = new TagArticleRelationDO();
            relation.setArticleId(blogArticle.getId());
            relation.setTagId(tagId);
            relation.setCreateUserId(userId);
            relation.setCreateTime(LocalDateTime.now());
            relation.setUpdateUserId(userId);
            relation.setUpdateTime(LocalDateTime.now());
            return relation;
        }).collect(Collectors.toList());

        // 5. 批量update and insert 
        System.out.println("🟡 即将 upsert tag relations = " + JSON.toJSONString(tagRelations));
        if(!tagRelations.isEmpty()){
            int upserted = dashboardTagArticleRelationMapper.updateAndInsertTagArticleRelations(tagRelations);
            if(upserted == 0){
                throw new ResponseException(ErrorCode.UPDATE_DB_FAILED.getCode(), "Failed to upsert tag-article relations for articleId = " + blogArticle.getId());
            }
        }
        System.out.println("💡 tagRelations = " + JSON.toJSONString(tagRelations));

        return 1;
    }

    /**
     * 批量删除博客文章
     *
     * @param ids 需要删除的博客文章主键
     * @return 结果
     */
    // @Override
    // public int deleteBlogArticleByIds(Integer[] ids, int updateUserId) {
    //     for (Integer id : ids) {
    //         BlogArticleDO blog = blogArticleMapper.selectBlogArticleById(id);
    //         deleteImgById(blog.getBannerImgId());
    //         dashboardTagArticleRelationMapper.deleteByArticleId(id, updateUserId);
    //     }

    //     return blogArticleMapper.deleteBlogArticleByIds(ids, updateUserId);
    // }
    //Hurely change to single query verion
    @Override
    public int deleteBlogArticleByIds(Integer[] ids, int updateUserId) {
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("Blog ID can not be null!");
        }
        List<Integer> idList = Arrays.stream(ids).collect(Collectors.toList());

        int result = blogArticleMapper.deleteBlogArticleWithRelations(idList, updateUserId);

        if (result == 0) {
            throw new RuntimeException("删除博客文章失败（未更新任何记录）");
        }

        return result;
    }

    /**
     * 删除博客文章信息
     *
     * @param id 博客文章主键
     * @return 结果
     */
    // @Override
    // public int deleteBlogArticleById(Integer id) {
    //     BlogArticleDO blog = blogArticleMapper.selectBlogArticleById(id);
    //     deleteImgById(blog.getBannerImgId());
    //     return blogArticleMapper.deleteBlogArticleById(id);
    // }

    //Hurely change to single query version
    @Override
    public int deleteBlogArticleById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("博客 ID 不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong(); // 动态获取当前登录用户
        return deleteBlogArticleByIds(new Integer[]{id}, userId.intValue());
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
        ImageDO image= blogImageMapper.findById(imgId);
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
