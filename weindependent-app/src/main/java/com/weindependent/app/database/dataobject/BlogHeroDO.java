package com.weindependent.app.database.dataobject;

import lombok.Data;
import java.util.Date;

@Data
public class BlogHeroDO {
    private Integer id;
    private Integer articleId;
    private String heroType;
    private Integer isDeleted;
    private Date updateTime;
    private Integer updateUserId;
    private Date createTime;
    private Integer createUserId;
}
