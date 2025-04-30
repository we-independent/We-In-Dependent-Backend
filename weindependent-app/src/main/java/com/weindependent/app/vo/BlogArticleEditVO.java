package com.weindependent.app.vo;

import lombok.Data;
import java.util.List;

@Data
public class BlogArticleEditVO {

    private Integer id;

    private String articleSourceType;

    private String sourceUrl;

    private Integer authorId;

    private Integer bannerImgId;

    private String bannerImgUrl;

    private String summary;

    private String title;

    private String content;

    private String articleStatus;

    private Integer categoryId;

    private Boolean isDeleted;

    private List<Integer> tagIdList;
}
