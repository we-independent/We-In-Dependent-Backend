package com.weindependent.app.vo.blog;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogCommentVO {
    private Integer id;

    private Integer articleId;

    private Integer parentCommentId;

    private String content;

    private LocalDateTime  createTime;

    private LocalDateTime  updateTime;

    private String visaType;

    private Integer isAuthor;

    private String username;

    private String avatar;


}
