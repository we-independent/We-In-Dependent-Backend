package com.weindependent.app.vo;

import java.util.Date;

import lombok.Data;

@Data
public class EditorPickVO {
    private Integer id;
    private String articleSourceType;
    private String sourceUrl;
    private Integer authorId;
    private Integer bannerImgId;
    private String summary;
    private String title;
    private Integer categoryId;
    private Date createTime;

}
