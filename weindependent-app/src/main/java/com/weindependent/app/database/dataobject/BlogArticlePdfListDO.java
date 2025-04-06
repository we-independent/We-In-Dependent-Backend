package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogArticlePdfListDO {

    private Integer id;
    private String fileName;
    private String filePath;
    private String downloadUrl;
    private LocalDateTime pdfFileGenerationTime;
    private Integer articleId;
    private Boolean isDeleted;
    private Integer createUserId;
    private LocalDateTime createTime;
    private Integer updateUserId;
    private LocalDateTime updateTime;
}
