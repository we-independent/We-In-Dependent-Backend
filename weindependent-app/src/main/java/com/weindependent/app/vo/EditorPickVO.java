package com.weindependent.app.vo;

import java.util.Date;

import lombok.Data;

@Data
public class EditorPickVO {
    private String heroType;        
    private Integer articleId;      
    private String title;          
    private Integer isDeleted;
    private Date updateTime;
    private Integer updateUserId;
    private Date createTime;
    private Integer createUserId;

}
