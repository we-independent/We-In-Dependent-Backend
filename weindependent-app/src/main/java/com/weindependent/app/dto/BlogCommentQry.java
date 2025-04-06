package com.weindependent.app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogCommentQry {

    /** 评论ID */
    private Integer id;

    /** 文章ID */
    private Integer articleId;

    /** 上级评论ID */
    private Integer parentCommentId;

    /** 评论内容 */
    private String content;

    /** 评论者ID */
    private Integer commentAuthorUserId;

    /** 0-未删除, 1-已删除 */
    private Boolean isDeleted;

    /** 创建人 */
    private Integer createUserId;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后更新人 */
    private Integer updateUserId;

    /** 最后更新时间 */
    private LocalDateTime  updateTime;

    private  Integer pageNum;

    private  Integer pageSize;
}
