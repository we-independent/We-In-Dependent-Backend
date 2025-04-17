package com.weindependent.app.vo;

import lombok.Data;

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
}
