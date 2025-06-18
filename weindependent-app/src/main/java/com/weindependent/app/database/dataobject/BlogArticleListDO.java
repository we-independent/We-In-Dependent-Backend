package com.weindependent.app.database.dataobject;
import com.weindependent.app.database.dataobject.BlogArticleListDO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 博客文章数据库blog_article
 * 
 * @author Hurely
 *    2025-04-5
 */

@Data
public class BlogArticleListDO
{
    /** 文章ID */
    private Integer id;

    /** 文章来源类型 */
    private String articleSourceType;

    /** 原始文章链接 */
    private String sourceUrl;

    /** 原始文章链接显示文字 */
    private String sourceUrlDisplayText;

    /** 作者ID */
    private Integer authorId;

    /**  阅读时间（以分钟为单位） **/
    private Integer readingTimeInMinutes;

    /** bannerID */
    private Integer bannerImgId;

    /** 文章摘要 */
    private String summary;

    /** 文章标题 */
    private String title;

    /** 文章内容 */
    private String content;

    /** 文章状态 */
    private String articleStatus;

    /** 文章分类 */
    private Integer categoryId;

    /** 0-未删除, 1-已删除 */
    private Boolean isDeleted;

    /** 创建人 */
    private Integer createUserId;

    /** 创建时间 */
    private LocalDateTime  createTime;

    /** 最后更新人 */
    private Integer updateUserId;

    /** 最后更新时间 */
    private LocalDateTime  updateTime;


}

