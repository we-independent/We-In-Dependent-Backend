package com.weindependent.app.database.dataobject;

import lombok.Data;
import java.util.Date;

@Data
public class BlogArticleDO {
    private Integer id;
    private String articleSourceType;
    private String sourceUrl;
    private Integer authorId;
    private Integer bannerImgId;
    private String summary;
    private String title;
    private String content;
    private String articleStatus;
    private Integer categoryId;
    private Integer isDeleted;
    private Integer createUserId;
    private Date createTime;
    private Integer updateUserId;
    private Date updateTime;

    
}

