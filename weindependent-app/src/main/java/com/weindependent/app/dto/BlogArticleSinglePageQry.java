package com.weindependent.app.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogArticleSinglePageQry {
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
    private Integer createUserId;
    private LocalDateTime createTime;
    private Integer updateUserId;
    private LocalDateTime updateTime;

    private List<BlogCommentQry> comments;

}
