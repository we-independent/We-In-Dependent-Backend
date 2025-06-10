package com.weindependent.app.vo.websiteoperation;

import lombok.Data;

import java.util.Date;

@Data
public class DashboardBlogHeroVO {
    private Integer id;
    private String heroType;
    private Integer articleId;
    private String articleTitle;
    private Integer isDeleted;
    private Date updateTime;
    private Integer updateUserId;
    private String updateUserName;
    private Date createTime;
    private Integer createUserId;
    private String createUserName;
}
