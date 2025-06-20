package com.weindependent.app.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogArticleSinglePageQry {
    private Integer id;
    private String articleSourceType;
    private String sourceUrl;
    private String sourceUrlDisplayText;
    private Integer authorId;
    private String authorName;
    private String readingtime;
    private Integer bannerImgId;
    private String bannerImageUrl;
    private String summary;
    private String title;
    private String content;
    private String articleStatus;
    private Integer categoryId;
    private Integer createUserId;
    private LocalDateTime createTime;
    private Integer updateUserId;
    private LocalDateTime updateTime;
    private List<String> tags;

    private List<BlogCommentQry> comments;
    private String disclaimer;

}
