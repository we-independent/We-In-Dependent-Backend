package com.weindependent.app.database.dataobject;
import lombok.Data;
import java.time.LocalDateTime;

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


}
