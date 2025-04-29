package com.weindependent.app.database.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogImageDO {
    private String id;
    private String category;
    private String fileName;
    private String fileKey;
    private String fileType;
    private String filePath;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
