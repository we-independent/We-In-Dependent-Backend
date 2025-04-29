package com.weindependent.app.convertor;

import com.weindependent.app.database.dataobject.BlogArticleDO;
import com.weindependent.app.vo.BlogArticleVO;

public class BlogConverter {
    public static BlogArticleVO toBlogVO(BlogArticleDO blogDO) {
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
}
