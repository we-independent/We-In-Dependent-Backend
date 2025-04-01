package com.weindependent.app.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogArticleQry {
    /** 文章ID */
    private Integer id;

    /** 文章来源类型 */
    private String articleSourceType;

    /** 原始文章链接 */
    private String sourceUrl;

    /** 作者ID */
    private Integer authorId;


    /** 文章标题 */
    private String title;


    /** 文章状态 */
    private String articleStatus;

    /** 文章分类 */
    private Integer categoryId;

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
