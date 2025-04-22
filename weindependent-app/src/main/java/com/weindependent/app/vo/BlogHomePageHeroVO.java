package com.weindependent.app.vo;

import lombok.Data;
import java.util.Date;

import java.util.List;

@Data
public class BlogHomePageHeroVO {

    private String heroType;

    private Integer articleId;

    private String articleTitle;

    private Integer categoryId;

    private String categoryName;

    private String  bannerImageUrl;

    private List<TagVO> tagVOList;

    // hero 区需要以下字段
    private Integer isDeleted;        
    private Date updateTime;        
    private Integer updateUserId;     
    private Date createTime;        
    private Integer createUserId;     
}
