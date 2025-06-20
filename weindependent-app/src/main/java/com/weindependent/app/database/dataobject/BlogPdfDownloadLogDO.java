package com.weindependent.app.database.dataobject;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class BlogPdfDownloadLogDO {

    /** primarykey */
    private Integer id;

    /** 文章ID */
    private Integer blogId;

    /** 下载的用户id */
    private Long userId;

    /** 用户下载时间 */
    private LocalDateTime downloadTime;

    /** 0-未删除, 1-已删除 */
    private Integer isDeleted;
    
    /** 最后更新人ID */
    private Long updateUserId;
    
    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /** 下载url */
    private String downloadUrl;

}
